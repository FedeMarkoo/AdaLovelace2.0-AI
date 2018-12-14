package Ada;

import BaseDeDatos.BD;
import Objetos.Objeto;

public class AdaLovelace extends Objeto {

	private Thread heart = new Thread() {
		public void run() {
			while (true)
				responder(Basico.escuchar());
		}
	};

	public AdaLovelace() {

	}

	@SuppressWarnings("deprecation")
	public boolean detener() {
		heart.interrupt();
		heart.stop();
		return !heart.isAlive();
	}

	public boolean iniciar() {
		heart.start();
		return heart.isAlive();
	}

	private String responder(String texto) {
		return Magico.magia(BD.decodificar(texto));
	}

}