package cadenaResponsabilidad;

import Objetos.Objeto;

public class Cadena {

	private static Objeto inicio;
	
	
	
	public static String comenzar(String[] deco) {
		return inicio.realizar(deco[0],deco);
	}

}
