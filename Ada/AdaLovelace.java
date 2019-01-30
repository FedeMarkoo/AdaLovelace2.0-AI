package Ada;

import java.awt.EventQueue;

import Ada.Acciones.Basico;
import Ada.Acciones.Magico;
import testeo.MainV;

public class AdaLovelace {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainV();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

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

	public static void caminar(int x, int y) {
		Basico.caminar(x, y);
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