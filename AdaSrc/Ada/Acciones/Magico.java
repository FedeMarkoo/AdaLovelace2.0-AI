package Ada.Acciones;

import java.io.File;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Scanner;

import Ada.AdaLovelace;
import Ada.AnalizadorSintactico.Tipo;
import BaseDeDatos.BD;
import BaseDeDatos.MapeoClase;
import Manager.Manager;

public class Magico {

	public static String ejecutar(String mensaje) {
		Tipo deco = BD.decodificar(mensaje.toLowerCase());
		return ejecutar(deco, mensaje);
	}

	public static String ejecutar(Tipo juego, String parametro) {
		if (juego == null)
			return "Perdon pero no entendi.";

		String clase = capitalizar(juego.clase());
		String metodo = juego.metodo();
		Class<?> objeto;
		try {
			objeto = Class.forName("Objetos." + clase);
		} catch (Exception e) {
			return noReconoceElSustantivo(clase, juego, parametro);
		}
		try {
			Method method = objeto.getMethod(metodo, String.class);
			String retorno = (String) method.invoke(1, parametro);
			return retorno;
		} catch (Exception e) {
			return noReconoceElVerbo(clase, metodo, juego, parametro);
		}
	}

	public static String capitalizar(String clase) {
		return (clase.charAt(0) + "").toUpperCase() + clase.substring(1).toLowerCase();
	}

	private static String noReconoceElSustantivo(String clase, Tipo tipo, String mensaje) {
		AdaLovelace.decir(
				"No entiendo lo que me estas pidiendo... \n" + clase + " es un sinomimo de un objeto ya registrado?");
		if (AdaLovelace.escuchar().contains("si")) {
			AdaLovelace.decir("Decime cual es su sinonimo");
			if (BD.cargarSinonimoSustantivo(clase, AdaLovelace.escuchar())) {
				AdaLovelace.decir("Carga realiada con exito");
				return ejecutar(tipo, mensaje);
			} else
				AdaLovelace.decir("No fue posible realizar la carga");
		} else {
			crearClase(clase);
		}
		return "No tengo ni idea que hacer";
	}

	private static String noReconoceElVerbo(String clase, String verbo, Tipo tipo, String mensaje) {
		AdaLovelace.decir(
				"No entiendo lo que me estas pidiendo... \n" + verbo + " es un sinomimo de una accion ya registrada?");
		if (AdaLovelace.escuchar().contains("si")) {
			AdaLovelace.decir("Decime cual es su sinonimo");
			if (BD.cargarSinonimoVerbo(verbo, AdaLovelace.escuchar())) {
				AdaLovelace.decir("Carga realiada con exito");
				return ejecutar(tipo, mensaje);
			} else
				AdaLovelace.decir("No fue posible realizar la carga");
		} else {
			AdaLovelace.decir("Desea agregar el codigo?");
			String codigo = "";
			if (AdaLovelace.escuchar().toLowerCase().contains("si")) {
				AdaLovelace.decir("Ingreselo");
				codigo = AdaLovelace.escuchar();
			}
			if (agregarMetodo(clase, verbo, codigo)) {
				AdaLovelace.decir("Carga de codigo realizada con exito");
				return ejecutar(tipo, mensaje);
			}
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
		return System.getenv("AdaLovelace2.0") + "Objetos." + clase + ".java";
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
		if (metodo.trim().length() > 1)
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
			BD.ingresarClase(new MapeoClase(clase, texto, Instant.now().toString()));
			AdaLovelace.decir("Clase-Actualizada-RECOMPILAR-COD:92929");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
