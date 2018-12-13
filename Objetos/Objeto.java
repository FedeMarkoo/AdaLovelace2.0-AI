package Objetos;

public class Objeto {
	public Objeto siguiente;

	public Objeto clase(String clase) {
		return this;
	}

	public String responder(String clase, String[] deco) {
		return "";
	}

	public String realizar(String clase, String[] deco) {
		if (this.getClass().toString().equals(clase))
			return responder(clase, deco);
		return siguiente.realizar(clase, deco);
	}
}
