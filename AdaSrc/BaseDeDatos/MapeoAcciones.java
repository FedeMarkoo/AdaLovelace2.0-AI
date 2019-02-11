package BaseDeDatos;

public class MapeoAcciones {

	private String sinonimo;
	private String accion;

	public MapeoAcciones() {
	}

	public MapeoAcciones(String accion, String sinonimo) {
		this.accion = accion;
		this.sinonimo = sinonimo;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getSinonimo() {
		return sinonimo;
	}

	public void setSinonimo(String sinonimo) {
		this.sinonimo = sinonimo;
	}

}