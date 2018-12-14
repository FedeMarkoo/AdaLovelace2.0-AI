package cadenaResponsabilidad;

import Acciones.Ultimo;
import Objetos.Objeto;

public class Cadena {

	private static Objeto inicio = new Ultimo();

	public static String comenzar(String[] deco) {
		return inicio.realizar(deco[0], deco);
	}

}
