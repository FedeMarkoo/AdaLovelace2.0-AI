package BaseDeDatos;

public class MapeoFrases {
	private String frase;
	private String verbo;

	public MapeoFrases() {

	}

	public MapeoFrases(String frase, String verbo) {
		this.frase = frase;
		this.verbo = verbo;
	}

	public String getFrase() {
		return frase;
	}

	public void setFrase(String frase) {
		this.frase = frase;
	}

	public String getVerbo() {
		return verbo;
	}

	public void setVerbo(String verbo) {
		this.verbo = verbo;
	}

}
