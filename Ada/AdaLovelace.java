package Ada;

public class AdaLovelace {

	private Thread heart = new Thread() {
		public void run() {
			while (true)
				Basico.decir(responder(escuchar()));
		}
	};

	public AdaLovelace() {

	}

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