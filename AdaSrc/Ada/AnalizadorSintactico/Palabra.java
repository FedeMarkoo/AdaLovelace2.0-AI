package Ada.AnalizadorSintactico;

import java.io.Serializable;
import java.util.ArrayList;

import BaseDeDatos.BDAda;

public class Palabra implements Serializable {
	/**
	 * 
	 */
	private static final String REGEX_NO_WORD = "[^a-zá-ú?]";

	private static final long serialVersionUID = 3509553821412970665L;
	public String palabra;
	public ArrayList<String> tipos;
	private boolean isPregunta;

	public Palabra(String palabra) {
		tipos = new ArrayList<String>();
		if (palabra.contains("?")) {
			this.palabra = palabra.replace("?", "");
			isPregunta = true;
			tipos.add("pregunta");
		} else {
			this.palabra = palabra;
			isPregunta = false;
		}
		String[] tiposarray = BDAda.tipoSintactico(palabra);
		if (tiposarray != null)
			for (String string : tiposarray)
				this.tipos.add(string);
	}

	public Palabra() {
	}

	public Palabra(String cad, String escuchar) {
		this.palabra = cad;
		tipos = new ArrayList<String>();
		for (String string : escuchar.split("[^a-z\\?]*")) {
			tipos.add(string.trim());
		}
	}

	public boolean match(String tipo) {
		return tipos.contains(tipo.replace("?", ""));
	}

	public boolean isAdverbio() {
		return tipos.size() == 1 && tipos.contains("adverbio");
	}

	public boolean isSigno() {
		return palabra.matches(REGEX_NO_WORD);
	}

	public boolean ignorar() {
		return tipos.contains("ignorar");
	}

	public boolean isPregunta() {
		return isPregunta;
	}
}
