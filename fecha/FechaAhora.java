package fecha;

import java.text.SimpleDateFormat;
import java.util.Date;

import Objetos.Fecha;

public class FechaAhora extends FechaGenerico {
	
	public FechaAhora(String s) {
		super(s);
	}

	public FechaAhora() {
		super();
	}
	
	
	@Override
	public String request(String mensaje) {
		if (mensaje.contains("ahora"))
			return handle( mensaje);
		else if( this.siguiente != null ) {
			return this.siguiente.request(mensaje);
		}else
			return null;
	}
	
	@Override
	public String handle(String mensaje) {
		return new SimpleDateFormat("hh:mm:ss - dd/MM/yyyy").format((Fecha.fecha2 == null) ? new Date() : Fecha.fecha2);
	}
	
}
