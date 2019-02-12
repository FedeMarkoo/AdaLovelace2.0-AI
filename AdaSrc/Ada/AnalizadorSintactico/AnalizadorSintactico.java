package Ada.AnalizadorSintactico;

import java.util.ArrayList;

import BaseDeDatos.BD;

public class AnalizadorSintactico {

	private static ArrayList<String> combinacionesSintacticas;

	private static ArrayList<String> cargarCombinaciones() {
		ArrayList<String> combo = new ArrayList<String>();
		combo.addAll(BD.getCombinacionesSintactico());
		return combo;
	}

	public static Tipo analizar(String mensaje) {
		combinacionesSintacticas = cargarCombinaciones();
		for (String combinacion : combinacionesSintacticas) {
			Tipo retorno = new Tipo(combinacion, mensaje);
			if (retorno.match())
				return retorno;
		}
		return null;
	}

}
