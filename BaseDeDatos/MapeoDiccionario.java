package BaseDeDatos;

public class MapeoDiccionario {

	private String palabra;
	private String tipo;

	public MapeoDiccionario(String palabra, String tipo) {
		this.palabra = palabra;
		this.tipo = tipo;
	}

	public MapeoDiccionario() {
		this.palabra = palabra;
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getPalabra() {
		return palabra;
	}

	public void setPalabra(String palabra) {
		this.palabra = palabra;
	}

}
