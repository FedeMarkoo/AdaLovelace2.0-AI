package Ada.Acciones;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class BDAda {
	private static ObjectInputStream bufferEntrada = null;
	private static ObjectOutputStream bufferSalida = null;

	public static void conectar() {
		Socket socket = null;
		while (socket == null) {
			try {
				socket = new Socket(InetAddress.getLocalHost(), 5051);
			} catch (Exception e) {
			}
			try {
				bufferEntrada = new ObjectInputStream(socket.getInputStream());
				bufferSalida = new ObjectOutputStream(socket.getOutputStream());
			} catch (Exception e) {
			}
			if (bufferSalida == null || bufferEntrada == null)
				try {
					socket.close();
					socket = null;
				} catch (Exception e) {
				}
		}

	}

	public void enviarComando(Object parametros) {
		try {
			bufferSalida.writeObject(parametros);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Object recibirComando() {
		try {
			return bufferEntrada.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
