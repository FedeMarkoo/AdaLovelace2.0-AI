package Ada.AnalizadorSintactico;

import java.util.ArrayList;
import java.util.List;

import BaseDeDatos.BD;

public class AnalizadorSintactico {

	private static ArrayList<String> combinacionesSintacticas = cargarCombinaciones();

	private static ArrayList<String> cargarCombinaciones() {
		ArrayList<String> combo = new ArrayList<>();
		combo.addAll(BD.getCombinacionesSintactico());
		return combo;
	}

	public static String[] analizar(String mensaje) {
		for (String combinacion : combinacionesSintacticas) {
			Tipo retorno = match(combinacion, mensaje);
			if (match!=null)
				return retorno;
		} // no se... deberia armar aca el retorno... pero se deberia modificar... que sea
								// lista de objetos y verbos...

		return false;
	}

	private static Tipo match(String combinacion, String mensaje) {
		ArrayList<Palabra> palabraTemp = new ArrayList<>();
		for (String palabra : mensaje.split(" ")) {
			palabraTemp.add(new Palabra(palabra));
		}

		Palabra[] palabras = (Palabra[]) palabraTemp.toArray();
		String[] tipos = combinacion.split(" ");
		int indice = 0;
		int desface = 0;
		int largoCombo = tipos.length, largoPalabra = palabras.length;
		// el desface es por los adverbios ya que no afentan al sentido de la oracion
		while (indice < largoCombo && indice + desface < largoPalabra) {
			Palabra palabra = palabras[indice + desface];
			String tipo = tipos[indice];
			if (!palabra.match(tipo))
				if (palabra.isAdverbio())
					desface++;
				else
					return null;
			else
				indice++;
		}
		return new Tipo(combinacion,mensaje);
	}

}
