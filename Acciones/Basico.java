package Acciones;

import testeo.Main;
import testeo.MainV;

public class Basico {

	public static void decir(String texto) {
		MainV.dice.append(texto);
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
		
		String a=MainV.escucha.getText().replace(".", "");
		MainV.escucha.setText("");
		return a.trim();
	}
}
