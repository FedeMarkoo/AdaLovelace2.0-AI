package Ada;

import BaseDeDatos.BD;
import Objetos.Objeto;

public class Magico {

	public static String magia(String[] deco) {
		Class<?> act;
		String clase = deco[0];
		String verbo = deco[1];

		try {

			act = Class.forName("Acciones." + clase);
		} catch (Exception e) {
			return noReconoceElSustantivo(clase);
		}

		try {
			String retorno = (String) act.getMethod(verbo).invoke(deco);
			return retorno;

		} catch (Exception e) {
			return noReconoceElVerbo(verbo);
		}

	}

	private static String noReconoceElSustantivo(String clase) {
		Basico.decir(
				"No entiendo lo que me estas pidiendo... \n" + clase + " es un sinomimo de un objeto ya registrado?");
		if (Basico.escuchar().contains("si")) {
			Basico.decir("Decime cual es su sinonimo");
			if (BD.cargarSinonimoSustantivo(clase, Basico.escuchar()))
				Basico.decir("Carga realiada con exito");
			else
				Basico.decir("No fue posible realizar la carga");
		} else {
			Basico.decir("Desea agregar el codigo?");
			if (Basico.escuchar().toLowerCase().contains("si")) {
				Basico.decir("Ingreselo");
				String codigo = Basico.escuchar();
				escribirClase(clase, codigo);
				return "Carga de codigo realizada con exito";
			}
		}
		return "No tengo ni idea que hacer";
	}

	public static String noReconoceElVerbo(String verbo) {
		Basico.decir(
				"No entiendo lo que me estas pidiendo... \n" + verbo + " es un sinomimo de una accion ya registrada?");
		if (Basico.escuchar().contains("si")) {
			Basico.decir("Decime cual es su sinonimo");
			if (BD.cargarSinonimoSustantivo(verbo, Basico.escuchar()))
				Basico.decir("Carga realiada con exito");
			else
				Basico.decir("No fue posible realizar la carga");
		} else {
			Basico.decir("Desea agregar el codigo?");
			if (Basico.escuchar().toLowerCase().contains("si")) {
				Basico.decir("Ingreselo");
				String codigo = Basico.escuchar();
				escribirMetodo(verbo, codigo);
				return "Carga de codigo realizada con exito";
			}

		}
		return "No tengo ni idea que hacer";
	}

	private static void escribirMetodo(String verbo, String codigo) {

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
		 */

	}

	private static void escribirClase(String clase, String codigo) {

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
		 */

		String texto = " package Objetos;		public class Modelo extends Objeto{}";
		if (texto.length() == 9) {
		}
	}

}
