package BaseDeDatos;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import Ada.Basico;

public class BD {

	private static SessionFactory factory;
	protected static Session session;

	public static String[] decodificar(String texto) {
		if (session == null) {
			Configuration conf = new Configuration();
			conf.configure("BaseDeDatos/hibernate.cfg.xml");
			factory = conf.buildSessionFactory();
			session = factory.openSession();
		}

		String verbo = "", adjetivo = "", sustantivo = "";

		for (String cad : texto.split(" ")) {

			String busqueda = getTipo(cad);

			if (busqueda == null) {
				Basico.decir("No identifico que tipo de palabra es " + cad
						+ ".\nNecesito que me digas si es un verbo, sustantivo, adjetivo o simplemente ignorar");
				ingresarTipo(cad, Basico.escuchar());
				busqueda = getTipo(cad);
			}

			if (!(busqueda != null && !busqueda.equals("ignorar")))
				switch (busqueda) {
				case "verbo":
					verbo = cad;
					break;
				case "adjetivo":
					adjetivo = cad;
					break;
				case "sustantivo":
					sustantivo = cad;
					break;
				}
		}
		return new String[] { sustantivo, verbo, adjetivo };
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
			return sinonimo.length() == 0 ? cad : getSinonimoVerbo(sinonimo);
		} catch (Exception e) {
		}
		return null;
	}

	public static String getSinonimoObjeto(String cad) {
		try {
			@SuppressWarnings("deprecation")
			Criteria cb = session.createCriteria(MapeoObjetos.class).add(Restrictions.eq("objeto", cad));
			String sinonimo = ((MapeoObjetos) cb.uniqueResult()).getSinonimo();
			return sinonimo.length() == 0 ? cad : getSinonimoVerbo(sinonimo);
		} catch (Exception e) {
		}
		return null;
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
}
