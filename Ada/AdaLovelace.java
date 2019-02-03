package Ada;

import Ada.Acciones.Basico;
import Ada.Acciones.Magico;

public class AdaLovelace {

	private Thread heart;

	public static void main(String a[]) {
		AdaLovelace ada = new AdaLovelace();
		ada.iniciar();
	}
	
	public AdaLovelace() {
		System.out.println("iniciando Ada");
		heart = new Thread() {
			public void run() {
				while (true) {
					String escuchar = escuchar();
					Basico.decir(responder(escuchar));
				}
			}
		};
	}

	public static void decir(String texto) {
		Basico.decir(texto);
	}

	public static String escuchar() {
		return Basico.escuchar();
	}

	@SuppressWarnings("deprecation")
	public boolean detener() {
		if (heart == null)
			return false;
		heart.interrupt();
		heart.stop();
		return !heart.isAlive();
	}

	public boolean iniciar() {
		if (heart == null)
			return false;
		heart.start();
		return heart.isAlive();
	}

	private String responder(String texto) {
		return Magico.ejecutar(texto);
	}

}