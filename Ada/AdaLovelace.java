package Ada;

import Acciones.Basico;
import Acciones.Objeto;
import BaseDeDatos.BD;

public class AdaLovelace extends Objeto {

	public AdaLovelace() {
		while(true)
			responder(Basico.escuchar());
	}
	
	public String responder(String texto) {

		String[] deco = decodificar(texto);

		return Magico.magia(deco);
	}

	private String[] decodificar(String texto) {
		String regex = "\\W(?:el|la|las|los)\\W";
		texto = texto.replaceAll(regex, "");

		return BD.decodificar(texto);
	}

	public Objeto clase(String clase) {
		if (clase.equals(this.getClass() + ""))
			return this;
		return siguiente.clase(clase);
	}
}
