package Ada;

import Ada.voz.Voz;
import testeo.MainV;

public class Basico {

	public static void decir(String texto) {
		MainV.dice.append("\n"+texto);
		Voz.speak(texto);
	}

	public static void caminar(int x, int y) {
		decir("x: " + x + "  y: " + y);
	}

	public static String escuchar() {
		while (!MainV.escucha.getText().contains("."))
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}

		String a = MainV.escucha.getText().replace(".", "").trim();
		MainV.escucha.setText("");
		MainV.dice.append("\n"+a);
		return a;
	}
}
