package Ada;

import Acciones.Basico;

public class Magico {

	public static String magia(String[] deco) {
		Class<?> act;
		String clase = deco[0];
		String verbo = deco[1];

		try {

			act = Class.forName("Acciones." + clase);
		} catch (Exception e) {
			return noReconoceElVerbo(clase);
		}
		
		try {
			String retorno = (String) act.getMethod(verbo).invoke(deco);
			return retorno;

		} catch (Exception e) {
			return noReconoceElVerbo(verbo);
		}

	}

	public static String noReconoceElVerbo(String verbo) {
		Basico.decir("No entiendo lo que me estas pidiendo... \nEs un sinomimo de una accion ya registrada?");
		if (Basico.escuchar().contains("si")) {

		} else {
			Basico.decir("Desea agregar el codigo?");
			if (Basico.escuchar().toLowerCase().contains("si")) {
				Basico.decir("Ingreselo");
				String codigo = Basico.escuchar();
				escribirMetodo(codigo);
				return "Carga de codigo realizada con exito";
			}

		}
		return "No tengo ni idea que hacer";
	}

	private static void escribirMetodo(String codigo) {

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

}
