package Ada.AnalizadorSintactico;

import java.util.ArrayList;

import BaseDeDatos.BD;

public class AnalizadorSintactico {

	private static ArrayList<String> combinacionesSintacticas = cargarCombinaciones();

	private static ArrayList<String> cargarCombinaciones() {
		ArrayList<String> combo = new ArrayList<>();
		combo.addAll(BD.getCombinacionesSintactico());
		return combo;
	}

	public static Tipo analizar(String mensaje) {
		for (String combinacion : combinacionesSintacticas) {
			Tipo retorno = new Tipo(combinacion, mensaje);
			if (retorno.match())
				return retorno;
		} // no se... deberia armar aca el retorno... pero se deberia modificar... que sea
			// lista de objetos y verbos...
		return null;
	}

}
