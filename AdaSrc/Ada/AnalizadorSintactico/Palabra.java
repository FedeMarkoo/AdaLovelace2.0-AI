package Ada.AnalizadorSintactico;

import java.io.Serializable;
import java.util.ArrayList;

import BaseDeDatos.BD;



public class Palabra implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3509553821412970665L;
	public String palabra;
	public ArrayList<String> tipos;

	public Palabra(String palabra) {
		this.palabra = palabra;
		tipos = new ArrayList<String>();
		String[] tiposarray = BD.tipoSintactico(palabra,0);

		if (tiposarray != null)
			for (String string :tiposarray)
				this.tipos.add(string);

	}

	public Palabra() {
	}

	public Palabra(String cad, String escuchar) {
		this.palabra = cad;
		tipos = new ArrayList<String>();
		for (String string : escuchar.split(",")) {
			tipos.add(string.trim());
		}
	}

	public boolean match(String tipo) {
		return tipos.contains(tipo);
	}

	public boolean isAdverbio() {
		return tipos.size() == 1 && tipos.contains("adverbio");
	}

	public boolean isSigno() {
		return palabra.matches("\\W*");
	}

	public boolean ignorar() {
		return tipos.contains("ignorar");
	}
}
