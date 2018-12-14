package Acciones;

public class Objeto {
	public Objeto siguiente;

	public Objeto() {
		
	}
	public Objeto clase(String clase) {
		return this;
	}

	public String responder(String accion) {
		return "no ameo ni idea";
	}

	public String realizar(String objeto, String accion) {
		String clase = this.getClass().getCanonicalName();
		if (clase.equals(objeto)||clase.equals("Acciones.Ultimo"))
			return responder(accion);
		return siguiente.realizar(objeto, accion);
	}
}
