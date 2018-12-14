package BaseDeDatos;

public class MapeoObjetos {

	private String objeto;
	private String sinonimo;
	private int cantidadDeLlamados;

	public MapeoObjetos() {
	}

	public MapeoObjetos(String objeto, int cant, String sinonimo) {
		this.objeto = objeto;
		cantidadDeLlamados = cant;
		this.sinonimo = sinonimo;
	}

	public String getObjeto() {
		return objeto;
	}

	public void setObjeto(String objeto) {
		this.objeto = objeto;
	}

	public int getCantidadDeLlamados() {
		return cantidadDeLlamados;
	}

	public void setCantidadDeLlamados(int cantidadDeLlamados) {
		this.cantidadDeLlamados = cantidadDeLlamados;
	}

	public String getSinonimo() {
		return sinonimo;
	}

	public void setSinonimo(String sinonimo) {
		this.sinonimo = sinonimo;
	}

}
