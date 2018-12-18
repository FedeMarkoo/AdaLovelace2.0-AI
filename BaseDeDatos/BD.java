package BaseDeDatos;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import Ada.AdaLovelace;

public class BD {

	private static SessionFactory factory;
	private static Session session;

	public static String[] decodificar(String texto) {
		String frase = decodificarPorFrase(texto);
		String[] deco = decodificarPorVerbo(texto);

		if (frase.length() != 0) {
			deco[1] = frase;
		}
		return deco;
	}

	private static String[] decodificarPorVerbo(String texto) {
		String verbo = "", adjetivo = "", sustantivo = "yo";

		for (String cad : texto.split(" ")) {

			String busqueda = getTipo(cad);

			if (busqueda == null) {
				AdaLovelace.decir("No identifico que tipo de palabra es " + cad
						+ ".\nNecesito que me digas si es un verbo, sustantivo, adjetivo o simplemente ignorar");
				ingresarTipo(cad, AdaLovelace.escuchar());
				busqueda = getTipo(cad);
			}

			if (busqueda != null)
				switch (busqueda) {
				case "verbo":
					verbo = getSinonimoVerbo(cad);
					break;
				case "adjetivo":
					adjetivo = cad;
					break;
				case "sustantivo":
					sustantivo = getSinonimoObjeto(cad);
					break;
				}
		}
		return new String[] { capitalizar(sustantivo), verbo, adjetivo };
	}

	@SuppressWarnings("deprecation")
	private static String decodificarPorFrase(String texto) {
		texto = texto.replace(" ", "%");
		try {
			SimpleExpression like = Restrictions.like("frase", texto, MatchMode.ANYWHERE);
			Criteria createCriteria = session.createCriteria(MapeoFrases.class);
			Criteria c = createCriteria.add(like);
			return ((MapeoFrases) c.uniqueResult()).getVerbo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getTipo(String cad) {
		try {
			@SuppressWarnings("deprecation")
			Criteria cb = session.createCriteria(MapeoDiccionario.class).add(Restrictions.eq("palabra", cad));
			return ((MapeoDiccionario) cb.uniqueResult()).getTipo();
		} catch (Exception e) {
		}
		return null;
	}

	public static String getSinonimoVerbo(String cad) {
		try {
			@SuppressWarnings("deprecation")
			Criteria cb = session.createCriteria(MapeoAcciones.class).add(Restrictions.eq("accion", cad));
			String sinonimo = ((MapeoAcciones) cb.uniqueResult()).getSinonimo();
			return getSinonimoVerbo(sinonimo);
		} catch (Exception e) {
		}
		return cad;
	}

	public static String getSinonimoObjeto(String cad) {
		try {
			@SuppressWarnings("deprecation")
			Criteria cb = session.createCriteria(MapeoObjetos.class).add(Restrictions.eq("objeto", cad));
			String sinonimo = ((MapeoObjetos) cb.uniqueResult()).getSinonimo();
			return getSinonimoObjeto(sinonimo);
		} catch (Exception e) {
		}
		return cad;
	}

	public static boolean ingresarTipo(String palabra, String tipo) {
		Transaction tx = session.beginTransaction();
		try {
			MapeoDiccionario res = new MapeoDiccionario(palabra.toLowerCase(), tipo);
			session.save(res);
			tx.commit();
			return true;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return false;
		}
	}

	public static boolean cargarSinonimoSustantivo(String objeto, String sinonimo) {
		Transaction tx = session.beginTransaction();
		try {
			MapeoObjetos res = new MapeoObjetos(objeto.toLowerCase(), 1, sinonimo);
			session.save(res);
			tx.commit();
			return true;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return false;
		}
	}

	public static boolean cargarSinonimoVerbo(String accion, String sinonimo) {
		Transaction tx = session.beginTransaction();
		try {
			MapeoAcciones res = new MapeoAcciones(accion.toLowerCase(), sinonimo);
			session.save(res);
			tx.commit();
			return true;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return false;
		}
	}

	public static boolean crearMetodo(String accion) {
		Transaction tx = session.beginTransaction();
		try {
			MapeoAcciones res = new MapeoAcciones(accion.toLowerCase(), "");
			session.save(res);
			tx.commit();
			return true;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return false;
		}
	}

	public static boolean crearClase(String clase) {
		Transaction tx = session.beginTransaction();
		try {
			MapeoObjetos res = new MapeoObjetos(clase.toLowerCase(), 1, "");
			session.save(res);
			tx.commit();
			return true;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return false;
		}
	}

	public static String getTipoDato(String atributo) {
		try {
			@SuppressWarnings("deprecation")
			Criteria cb = session.createCriteria(MapeoTipoDeDato.class).add(Restrictions.eq("atributo", atributo));
			return ((MapeoTipoDeDato) cb.uniqueResult()).getTipo();
		} catch (Exception e) {
		}
		return null;
	}

	public static String capitalizar(String clase) {
		return (clase.charAt(0) + "").toUpperCase() + clase.substring(1).toLowerCase();
	}

	public static void conectarBD() {
		Configuration conf = new Configuration();
		conf.configure("BaseDeDatos/hibernate.cfg.xml");
		factory = conf.buildSessionFactory();
		session = factory.openSession();
	}

}