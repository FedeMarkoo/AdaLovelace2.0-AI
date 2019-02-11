package Ada.voz;

import java.util.ArrayList;

public class Voz {

	private static ArrayList<String> lista = new ArrayList<>();
	private static Thread hilo = new Thread() {
		public void run() {
			this.setName("Voz");
			this.interrupt();
			while (true) {
				while (!lista.isEmpty()) {
					decir(lista.remove(0));
					esperar();
				}
				this.interrupt();
			}
		}

		private void esperar() {
			try {
				sleep(500);
			} catch (Exception e) {
			}
		}
	};

	public static void speak(String texto) {
		lista.add(texto);
		if (!hilo.isAlive())
			hilo.start();
	}

	private static void decir(String texto) {
		try {
			Runtime.getRuntime().exec("espeak -ves-la \"" + texto + "\"");
		} catch (Exception e) {
			System.out.println("fallo al hablar");
		}
	}

	public static void main(String[] args) {
		Voz.decir("Buenos dias, soy ada lovelace, ando para el culo y hablo como traba");
	}
}
