package Ada;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.Scanner;

import BaseDeDatos.BD;

public class Magico {

	public static String ejecutar(String mensaje) {
		String[] deco = BD.decodificar(mensaje.toLowerCase());
		return ejecutar(deco[0], deco[1], mensaje);
	}

	public static String ejecutar(String clase, String metodo) {
		return ejecutar(clase, metodo, "");
	}

	public static String ejecutar(String clase, String metodo, String parametro) {
		Class<?> act;
		try {
			act = Class.forName("Objetos." + clase);
		} catch (Exception e) {
			return noReconoceElSustantivo(clase);
		}
		try {
			Method method = act.getMethod(metodo, String.class);
			String retorno = (String) method.invoke(1,parametro);
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
		}
		return "No tengo ni idea que hacer";
	}

	private static String noReconoceElVerbo(String clase, String verbo) {
		AdaLovelace.decir(
				"No entiendo lo que me estas pidiendo... \n" + verbo + " es un sinomimo de una accion ya registrada?");
		if (AdaLovelace.escuchar().contains("si")) {
			AdaLovelace.decir("Decime cual es su sinonimo");
			if (BD.cargarSinonimoVerbo(verbo, AdaLovelace.escuchar()))
				AdaLovelace.decir("Carga realiada con exito");
			else
				AdaLovelace.decir("No fue posible realizar la carga");
		} else {
			AdaLovelace.decir("Desea agregar el codigo?");
			String codigo = "";
			if (AdaLovelace.escuchar().toLowerCase().contains("si")) {
				AdaLovelace.decir("Ingreselo");
				codigo = AdaLovelace.escuchar();
			}
			if (agregarMetodo(clase, verbo, codigo))
				return "Carga de codigo realizada con exito";
			return "No fue posible realizar la carga";
		}
		return "No tengo ni idea que hacer";
	}

	private static String leerClase(String clase) {
		String texto = "";
		Scanner f;
		try {
			f = new Scanner(new File(getClassName(clase)));
			while (f.hasNextLine())
				texto += f.nextLine();
			f.close();
		} catch (Exception e) {
		}
		return texto;
	}

	private static String getClassName(String clase) {
		return System.getenv("AdaLovelace2.0") + "Objetos\\" + clase + ".java";
	}

	private static boolean crearClase(String clase) {
		String texto = "package Objetos;\n\npublic class " + clase + " extends Objeto\n{\n}";

		return escribirArchivo(clase, texto);// && BD.crearClase(clase);
	}

	public static boolean agregarAtributo(String clase, String atributo) {
		String texto = leerClase(clase);
		texto = texto.replaceFirst("{", "{\n\t public " + BD.getTipoDato(atributo) + " " + atributo + ";");
		return escribirArchivo(clase, texto);
	}

	public static boolean agregarMetodo(String clase, String metodo, String codigo) {
		String texto = leerClase(clase);
		if(metodo.trim().length() > 1)
			return true;
		texto = texto.substring(0, texto.lastIndexOf("}"));
		if (!codigo.contains("return"))
			if (codigo.length() < 4)
				codigo += "return \"Pendiente de implementacion\";";
			else
				codigo += "return \"faltante de return\";";
		texto += "\tpublic static String " + metodo + "(String mesaje){\n" + codigo + "\n}\n}";

		return escribirArchivo(clase, texto);// && BD.crearMetodo(metodo);
	}

	private static boolean escribirArchivo(String clase, String texto) {
		try {
			BufferedWriter f = new BufferedWriter(new FileWriter(getClassName(clase)));
			f.write(texto);
			f.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
