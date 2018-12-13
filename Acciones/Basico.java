package Acciones;

import java.util.Scanner;

public class Basico {

	public static void decir(String texto) {
		System.out.println(texto);
	}

	public static void caminar(int x, int y) {
		System.out.println("x: " + x + "  y: " + y);
	}

	public static String escuchar() {
		Scanner scanner = new Scanner(System.in);
		String nextLine = scanner.nextLine();
		scanner.close();
		return nextLine;
	}
}
