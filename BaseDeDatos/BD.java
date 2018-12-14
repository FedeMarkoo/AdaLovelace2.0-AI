package BaseDeDatos;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import Acciones.Basico;

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

			String busqueda = buscar(cad, MapeoDiccionario.class);

			if (busqueda == null) {
				Basico.decir("No identifico que tipo de palabra es " + cad
						+ ".\nNecesito que me digas si es un verbo, sustantivo, adjetivo o simplemente ignorar");
				ingresarTipo(cad, Basico.escuchar());
				busqueda = buscar(cad, MapeoDiccionario.class);
			}

			if (busqueda != null)
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

	@SuppressWarnings("all")
	public static String buscar(String texto, Class clase) {
		try {
			Criteria cb = session.createCriteria(clase).add(Restrictions.eq("palabra", texto));
			if (cb != null && cb.list() != null && !cb.list().isEmpty()) {
				if (MapeoAcciones.class == clase)
					return ((MapeoAcciones) cb.uniqueResult()).getAccion();
				if (MapeoDiccionario.class == clase)
					return ((MapeoDiccionario) cb.uniqueResult()).getTipo();
			}
		} catch (Exception e) {
			e.printStackTrace();
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
}
