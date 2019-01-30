package BaseDeDatos;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import Ada.AnalizadorSintactico.AnalizadorSintactico;
import Ada.AnalizadorSintactico.Palabra;
import Ada.AnalizadorSintactico.Tipo;

public class BD {

	private static SessionFactory factory;
	private static Session session = conectar();
	private static final Pattern compile = Pattern.compile("<br>\\s*<i>\\s*(\\w+)\\s*<\\/i>");

	private static Session conectar() {
		Configuration conf = new Configuration();
		conf.configure("BaseDeDatos/hibernate.cfg.xml");
		factory = conf.buildSessionFactory();
		return factory.openSession();
	}

	public static Tipo decodificar(String texto) {
		Tipo deco = decodificarPorFrase(texto);
		return deco;
	}

//	private static String decodificarTipo(String cad) {
//		String busqueda = getTipo(cad);
//
//		if (busqueda == null)
//			busqueda = googlearTipo(cad);
//		if (busqueda != null)
//			ingresarTipo(busqueda);
//		else
//			busqueda = ingresarTipo(cad);
//		return busqueda;
//	}

//	private static String ingresarTipo(String cad) {
//		String busqueda;
//		AdaLovelace.decir("No identifico que tipo de palabra es " + cad
//				+ ".\nNecesito que me digas si es un verbo, sustantivo, adjetivo o simplemente ignorar");
//		ingresarTipo(new Palabra(cad, AdaLovelace.escuchar()));
//		busqueda = getTipo(cad);
//		return busqueda;
//	}

	private static Tipo decodificarPorFrase(String texto) {
		return AnalizadorSintactico.analizar(texto);
	}

	public static String getTipo(String cad) {
		try {
			@SuppressWarnings("deprecation")
			Criteria cb = session.createCriteria(MapeoDiccionario.class).add(Restrictions.eq("palabra", cad));
			return ((MapeoDiccionario) cb.uniqueResult()).getTipo();
		} catch (Exception e) {
			return null;
		}
	}

	public static String getSinonimoVerbo(String cad) {
		try {
			@SuppressWarnings("deprecation")
			Criteria cb = session.createCriteria(MapeoAcciones.class).add(Restrictions.eq("accion", cad));
			String sinonimo = ((MapeoAcciones) cb.uniqueResult()).getSinonimo();
			return getSinonimoVerbo(sinonimo);
		} catch (Exception e) {
			return cad;
		}
	}

	public static String getSinonimoObjeto(String cad) {
		try {
			@SuppressWarnings("deprecation")
			Criteria cb = session.createCriteria(MapeoObjetos.class).add(Restrictions.eq("objeto", cad));
			String sinonimo = ((MapeoObjetos) cb.uniqueResult()).getSinonimo();
			return getSinonimoObjeto(sinonimo);
		} catch (Exception e) {
			return cad;
		}
	}

	public static boolean ingresarPalabra(Palabra palabra) {
		Transaction tx = session.beginTransaction();
		try {
			for (String tipo : palabra.tipos) {
				session.save(new MapeoSintactico(palabra.palabra, tipo));
				tx.commit();
			}
			return true;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return false;
		}
	}

	public static boolean ingresarTipo(String palabra, String tipo) {
		Transaction tx = session.beginTransaction();
		try {
			session.save(new MapeoSintactico(palabra, tipo));
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

//	private static String googlearTipo(String palabra) {
//		String linea;
//		linea = getContenido(palabra, "https://es.thefreedictionary.com/" + palabra);
//		Matcher m = Pattern.compile("<h2>" + palabra + ".{0,500}(sustantivo|adjetivo|verbo)").matcher(linea);
//		if (m.find())
//			return m.group(1);
//
//		return "ignorar";
//
//	}

	private static String[] googlearTipos(String palabra) {
		ArrayList<String> tipos = new ArrayList<>();
		String linea = getContenido(palabra, "https://es.thefreedictionary.com/" + palabra);
		Matcher m = compile.matcher(linea);
		while (m.find()) {
			String tipo = m.group(1).toLowerCase();
			if (!tipos.contains(tipo))
				tipos.add(tipo);
		}
		return (String[]) tipos.toArray();
	}

	private static String getContenido(String palabra, String url) {
		HttpURLConnection connection;
		String linea;
		try {

			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.addRequestProperty("User-Agent", "Mozilla/4.76");
			connection.setConnectTimeout(15000);
			connection.setReadTimeout(15000);
			connection.setInstanceFollowRedirects(true);
			connection.connect();
			InputStream inputStream = connection.getInputStream();
			InputStreamReader inputReader = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader lector = new BufferedReader(inputReader);
			linea = "";
			String temp = "";
			while ((temp = lector.readLine()) != null)
				linea += temp;

		} catch (Exception e) {
			return null;
		}
		return linea;
	}

	@SuppressWarnings("all")
	public static String[] tipoSintactico(String palabra) {
		try {
			Criteria cb = session.createCriteria(MapeoSintactico.class).add(Restrictions.eq("palabra", palabra));
			List<MapeoSintactico> temp = cb.list();
			if (temp.isEmpty()) {
				getTipos(palabra);
				return tipoSintactico(palabra);
			}

			String[] retorno = new String[temp.size()];
			while (!temp.isEmpty())
				retorno[temp.size() - 1] = temp.remove(0).getTipo();
			return retorno;
		} catch (Exception e) {
			return null;
		}
	}

	private static void getTipos(String palabra) {
		String [] tipos=googlearTipos(palabra);
		for (String tipo : tipos) {
			ingresarTipo(palabra, tipo);
		}
	}

	@SuppressWarnings("all")
	public static List<String> getCombinacionesSintactico() {
		try {
			Criteria cb = session.createCriteria(MapeoTipoDeDato.class);
			return cb.list();
		} catch (Exception e) {
			return null;
		}
	}

}