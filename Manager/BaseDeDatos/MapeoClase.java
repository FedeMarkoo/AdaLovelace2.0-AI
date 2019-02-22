package BaseDeDatos;

import java.io.Serializable;

public class MapeoClase  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7852485139670858093L;
	private String nombre;
	private String codigo;
	private String fecha;

	public MapeoClase(String nombre, String codigo, String fecha) {
		this.nombre = nombre;
		this.codigo = codigo;
		this.fecha = fecha;
	}

	public MapeoClase() {

	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

}
