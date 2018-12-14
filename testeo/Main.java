package testeo;

import java.util.Scanner;

import Ada.AdaLovelace;

public class Main {

	public static void main(String[] a) {
		AdaLovelace ada = new AdaLovelace();
		Scanner scan = new Scanner(System.in);
		boolean o =true;
		while(o) {
			ada.responder(scan.nextLine());
		}
		scan.close();
	}
}
