package testeo;

import java.util.Scanner;

import Ada.AdaLovelace;

public class Main {

	private static String nextLine;
	public static Scanner scan;

	public static void main(String[] a) {
		AdaLovelace ada = new AdaLovelace();
		boolean o = true;
		scan = new Scanner(System.in);
		while (o) {
			try {
				nextLine = scan.nextLine();
				ada.responder(nextLine);
			} catch (Exception e) {
				System.out.println("error en scaner");
				scan.close();
				return;
			}
		}
	}
}
