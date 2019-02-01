package Ada;

import java.awt.EventQueue;

import Ada.Acciones.Basico;
import Ada.Acciones.Magico;
import testeo.Manager;

public class AdaLovelace {
	
	private Thread heart = new Thread() {
		public void run() {
			while (true) {
				String escuchar = escuchar();
				if (escuchar.trim().length() != 0)
					Basico.decir(responder(escuchar));
			}
		}
	};

	public static void decir(String texto) {
		Basico.decir(texto);
	}

	public static String escuchar() {
		return Basico.escuchar();
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
		return Magico.ejecutar(texto);
	}

}