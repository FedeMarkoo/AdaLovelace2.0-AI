package BaseDeDatos;

import javax.persistence.*;

public class MapeoSintactico {

	private String palabra;
	private String tipo;
	private int id;

	public MapeoSintactico(String palabra, String tipo) {
		this.palabra = palabra;
		this.tipo = tipo;
	}

	public MapeoSintactico() {
	}

	public String getPalabra() {
		return palabra;
	}

	public String getTipo() {
		return tipo;
	}

	public void setPalabra(String palabra) {
		this.palabra = palabra;
	}

	public void setTipo(String tipos) {
		this.tipo = tipos;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
