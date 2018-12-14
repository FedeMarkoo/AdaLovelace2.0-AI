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

		for (String cad : texto.split(" ")) {

			String busqueda = buscar(cad, MapeoDiccionario.class);

			if (busqueda == null) {
				Basico.decir("No identifico que tipo de palabra es " + cad + ".\nNecesito que me lo digas");
				ingresarTipo(cad, Basico.escuchar());
			}
		}
		return null;
	}

	@SuppressWarnings("all")
	public static String buscar(String texto, Class clase) {
		try {
			Criteria cb = session.createCriteria(clase).add(Restrictions.eq("clase", texto));
			if (cb != null && cb.list() != null && !cb.list().isEmpty()) {
				if (MapeoAcciones.class == clase)
					return ((MapeoAcciones) cb).getAccion();
				if (MapeoDiccionario.class == clase)
					return ((MapeoDiccionario) cb).getTipo();
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
