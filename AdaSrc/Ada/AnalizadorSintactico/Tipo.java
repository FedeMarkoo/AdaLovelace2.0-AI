package Ada.AnalizadorSintactico;

import java.io.Serializable;
import java.util.ArrayList;

import BaseDeDatos.BDAda;

public class Tipo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8357347381264347906L;
	private boolean match;
	private JuegoPalabras juego;

	public Tipo(String combinacion, String mensaje) {
		juego = new JuegoPalabras();
		ArrayList<Palabra> palabraTemp = new ArrayList<Palabra>();
		for (String palabra : mensaje.split("[^a-z]")) {
			palabraTemp.add(new Palabra(palabra));
		}

		Palabra[] palabras = (Palabra[]) palabraTemp.toArray(new Palabra[0]);
		String[] tipos = combinacion.split(" ");
		int indice = 0;
		int desface = 0;
		int largoCombo = tipos.length, largoPalabra = palabras.length;
		// el desface es por los adverbios ya que no afentan al sentido de la oracion
		while (indice < largoCombo && indice + desface < largoPalabra) {
			Palabra palabra = palabras[indice + desface];
			String tipo = tipos[indice];
			if (!palabra.match(tipo))
				if (palabra.isAdverbio() || palabra.isSigno() || palabra.ignorar())
					desface++;
				else if (opcional(tipo)) {
					desface--;
					indice++;
				} else {
					match = false;
					return;
				}
			else {
				indice++;
				switch (tipo) {
				case "sustantivo":
					juego.addSustantivo(palabra.palabra);
					break;
				case "adjetivo":
				case "determinante":
					juego.addAdjetivo(palabra.palabra);
					break;
				case "interjección":
				case "adverbio":
				case "verbo":
					juego.addVerbo(palabra.palabra);
					break;
				}
			}
		}
		match = true;
	}

	private boolean opcional(String tipo) {
		return tipo.contains("?");
	}

	public boolean match() {
		return match;
	}

	public JuegoPalabras getJuegoPalabras() {
		return juego;
	}

	public String clase() {
		ArrayList<String> sustantivos = getJuegoPalabras().getSustantivos();
		if (sustantivos.isEmpty())
			return "yo";
		return BDAda.getSinonimoObjeto(sustantivos.get(0));
	}

	public String metodo() {
		return BDAda.getSinonimoVerbo(getJuegoPalabras().getVerbo().get(0));
	}
}
