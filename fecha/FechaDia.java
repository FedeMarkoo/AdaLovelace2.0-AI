package fecha;

import java.text.SimpleDateFormat;
import java.util.Date;

import Objetos.Fecha;

public class FechaDia extends FechaGenerico {
	public FechaDia(String s) {
		super(s);
	}

	public FechaDia() {
		super();
	}
	
	@Override
	public String request(String mensaje) {
		if (mensaje.contains("dia"))
			return handle( mensaje);
		else if( this.siguiente != null ) {
			return this.siguiente.request(mensaje);
		}else
			return null;
	}
	
	@Override
	public String handle(String mensaje) {
		return new SimpleDateFormat("EEEEEEEEE").format((Fecha.fecha2 == null) ? new Date() : Fecha.fecha2);
	}
	
}
