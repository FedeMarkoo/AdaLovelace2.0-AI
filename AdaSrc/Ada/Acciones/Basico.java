package Ada.Acciones;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import Ada.voz.Voz;

public class Basico {

	private static DataInputStream bufferEntrada = null;
	private static DataOutputStream bufferSalida = null;

	private static void conectar() {
		Socket socket = null;
		while (socket == null)
			try {
				socket = new Socket(InetAddress.getLocalHost(), 5050);
			} catch (Exception e) {
			}
		try {
			bufferEntrada = new DataInputStream(socket.getInputStream());
			bufferSalida = new DataOutputStream(socket.getOutputStream());
		} catch (

		Exception e) {
		}
		if (bufferSalida == null)
			try {
				socket.close();
			} catch (Exception e) {
			}
	}

	/**
	 * La idea seria hacer que separe por silbas (podria se una regex que separe por
	 * [aeiou]) despues enviar la silaba a un sintetizador y tambien a un gestor de
	 * movimientos de cara para que parezca que habla
	 * 
	 * @param texto
	 */
	public static void decir(String texto) {
		if (bufferSalida == null)
			conectar();
		try {
			bufferSalida.writeUTF(texto);
		} catch (Exception e) {
		}
		Voz.speak(texto);
	}

	public static void caminar(int x, int y) {
		decir("x: " + x + "  y: " + y);
	}

	public static String escuchar() {
		if (bufferEntrada == null)
			conectar();
		try {
			return bufferEntrada.readUTF();
		} catch (IOException e) {
			return "";
		}
	}
}
