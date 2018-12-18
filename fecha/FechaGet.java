package fecha;

import java.text.SimpleDateFormat;
import java.util.Date;

import Objetos.Fecha;

public class FechaGet extends FechaGenerico {
	public FechaGet(String s) {
		super(s);
	}

	public FechaGet() {
		super();
	}

	@Override
	public String request(String mensaje) {
		if (mensaje.contains("getfecha"))
			return handle(mensaje);
		else if (this.siguiente != null) {
			return this.siguiente.request(mensaje);
		} else
			return null;
	}

	@Override
	public String handle(String mensaje) {
		return new SimpleDateFormat("dd/MM/yyyy").format((Fecha.fecha2 == null) ? new Date() : Fecha.fecha2);
	}

}
