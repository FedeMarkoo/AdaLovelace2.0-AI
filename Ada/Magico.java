package Ada;

import java.io.FileWriter;
import java.util.Scanner;

import BaseDeDatos.BD;

public class Magico {

	public static String magia(String[] deco, String mensaje) {
		Class<?> act;
		String clase = deco[0];
		String verbo = deco[1];

		try {
			act = Class.forName("Objetos." + clase);
		} catch (Exception e) {
			return noReconoceElSustantivo(clase);
		}

		try {
			String retorno = (String) act.getMethod(verbo).invoke(mensaje);
			return retorno;

		} catch (Exception e) {
			return noReconoceElVerbo(clase, verbo);
		}
	}

	public static String ejecutar(String mensaje) {
		return magia(BD.decodificar(mensaje), mensaje);
	}

	public static String ejecutar(String clase, String metodo) {
		Class<?> act;
		try {
			act = Class.forName("Objetos." + clase);
		} catch (Exception e) {
			return noReconoceElSustantivo(clase);
		}
		try {
			String retorno = (String) act.getMethod(metodo).invoke("");
			return retorno;
		} catch (Exception e) {
			return noReconoceElVerbo(clase, metodo);
		}
	}

	private static String noReconoceElSustantivo(String clase) {
		AdaLovelace.decir(
				"No entiendo lo que me estas pidiendo... \n" + clase + " es un sinomimo de un objeto ya registrado?");
		if (AdaLovelace.escuchar().contains("si")) {
			AdaLovelace.decir("Decime cual es su sinonimo");
			if (BD.cargarSinonimoSustantivo(clase, AdaLovelace.escuchar()))
				AdaLovelace.decir("Carga realiada con exito");
			else
				AdaLovelace.decir("No fue posible realizar la carga");
		} else {
			crearClase(clase);
			AdaLovelace.decir("Desea agregar el codigo?");
			if (AdaLovelace.escuchar().toLowerCase().contains("si")) {
				AdaLovelace.decir("Ingreselo");
				return "Carga de codigo realizada con exito";
			}
		}
		return "No tengo ni idea que hacer";
	}

	public static String noReconoceElVerbo(String clase, String verbo) {
		AdaLovelace.decir(
				"No entiendo lo que me estas pidiendo... \n" + verbo + " es un sinomimo de una accion ya registrada?");
		if (AdaLovelace.escuchar().contains("si")) {
			AdaLovelace.decir("Decime cual es su sinonimo");
			if (BD.cargarSinonimoSustantivo(verbo, AdaLovelace.escuchar()))
				AdaLovelace.decir("Carga realiada con exito");
			else
				AdaLovelace.decir("No fue posible realizar la carga");
		} else {
			AdaLovelace.decir("Desea agregar el codigo?");
			if (AdaLovelace.escuchar().toLowerCase().contains("si")) {
				AdaLovelace.decir("Ingreselo");
				String codigo = AdaLovelace.escuchar();
				agregarMetodo(clase, verbo, codigo);
				return "Carga de codigo realizada con exito";
			}

		}
		return "No tengo ni idea que hacer";
	}

	private static String leerClase(String clase) {
		String texto = "";
		Scanner f = new Scanner(getClassName(clase));
		while (f.hasNextLine())
			texto += f.nextLine();
		f.close();
		return texto;
	}

	private static String getClassName(String clase) {
		return System.getenv("AdaLovelace2.0") + "Objeto\\" + clase + ".java";
	}

	private static boolean crearClase(String clase) {
		String texto = "package Objetos;\n\npublic class " + clase + " extends Objeto\n{\n}";
		return escribirArchivo(clase, texto);
	}

	/**
	 * bardo para despues.... pero la onda seria que tome el nombre del objeto al
	 * que pertenece la accion y se fije si existe el archivo, si existe se agrega
	 * el metodo con el nombre de la accion y su codigo si no existe algo salio
	 * mal... ya que de no existir la clase, esta se debe crear en una instancia
	 * anterior, la cual es cuando se reconoce el objeto y de no existir se le
	 * establecen sus propiedades
	 * 
	 * para agregar un metodo se debe reemplazar la ultima llave y para agregar un
	 * atributo se debe reemplazar la primer llave
	 * 
	 * @return
	 */

	private static boolean agregarAtributo(String clase, String atributo) {
		String texto = leerClase(clase);
		texto = texto.replaceFirst("{", "{\n\t public " + BD.getTipoDato(atributo) + " " + atributo + ";");
		return escribirArchivo(clase, texto);
	}

	private static boolean agregarMetodo(String clase, String metodo, String codigo) {

		String texto = leerClase(clase);

		texto = texto.substring(0, texto.lastIndexOf("}") - 1);
		texto += "\t public static String " + metodo + "(){\n" + codigo + "\n}\n}";

		return escribirArchivo(clase, texto);
	}

	private static boolean escribirArchivo(String clase, String texto) {
		try {
			FileWriter f = new FileWriter(getClassName(clase));
			f.write(texto);
			f.close();
			return BD.crearClase(clase);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
