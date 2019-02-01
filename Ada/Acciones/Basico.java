package Ada.Acciones;

import Ada.voz.Voz;
import testeo.Manager;

public class Basico {

	/**
	 * La idea seria hacer que separe por silbvas (pordria se una regex que separe por [aeiou])
	 * despues enviar la silaba a un sintetizador y tambien a un gestor de movimientos de cara para que parezca que habla
	 * @param texto
	 */
	public static void decir(String texto) {
		Manager.dice.append("\n"+texto);
		Voz.speak(texto);
	}

	public static void caminar(int x, int y) {
		decir("x: " + x + "  y: " + y);
	}

	public static String escuchar() {
		while (!Manager.escucha.getText().contains("."))
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}

		String a = Manager.escucha.getText().replace(".", "").trim();
		Manager.escucha.setText("");
		Manager.dice.append("\n"+a);
		return a;
	}
}
