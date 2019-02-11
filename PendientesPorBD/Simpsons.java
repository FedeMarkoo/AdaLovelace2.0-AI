package PendientesPorBD;

import Objetos.Objeto;

/** 
 * linguo muerto.
 */
public class Simpsons extends Objeto{

	public String getSimpsons()
	{
		return new SimpsonsBD().getSimpsonsCaps();		
	}
}
