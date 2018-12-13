package Ada;

import BaseDeDatos.BD;
import Objetos.Objeto;
import cadenaResponsabilidad.Cadena;

public class AdaLovelace extends Objeto {

	public String responder(String texto) {

		String[] deco = decodificar(texto);

		return Cadena.comenzar(deco);
	}

	private String[] decodificar(String texto) {
		String regex = "el|la|las|los";
		texto = texto.replaceAll(regex, "");

		return BD.decodificar(texto);
	}

	public Objeto clase(String clase){
  	if(clase.equals(this.getClass()+""))
      return this;
    return siguiente.clase(clase);
  }  
}
