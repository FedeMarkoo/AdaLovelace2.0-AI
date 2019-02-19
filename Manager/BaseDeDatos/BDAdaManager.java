package BaseDeDatos;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BDAdaManager {
	private static ObjectInputStream bufferEntrada = null;
	private static ObjectOutputStream bufferSalida = null;
	private static ServerSocket serversock;
	private static Socket socket;

	static {
		conectar();
	}

	public static void conectar() {
		socket = null;
		while (socket == null) {
			try {
				serversock = new ServerSocket(5051);
				socket = serversock.accept();
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

	public static void enviarComando(Object parametros) {
		try {
			bufferSalida.writeObject(parametros);
		} catch (Exception e) {
			conectar();
		}
	}

	public static Object recibirComando() {
		try {
			return bufferEntrada.readObject();
		} catch (Exception e) {
			conectar();
			return recibirComando();
		}
	}
}
