package BaseDeDatos;

import java.io.IOException;
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
		try {
			serversock = new ServerSocket(5051);
		} catch (IOException e1) {
		}
		try {
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
			} catch (Exception e) {
			}
	}

	public static void enviarComando(Object parametros) {
		try {
			bufferSalida.writeObject(parametros);
			System.out.println("Se envia " + parametros);
		} catch (Exception e) {
			e.printStackTrace();
			conectar();
			enviarComando(parametros);
		}
	}

	public static Object recibirComando() {
		try {
			Object retorno = bufferEntrada.readObject();
			System.out.println("Se recibe " + retorno);
			return retorno;
		} catch (Exception e) {
			conectar();
			return recibirComando();
		}
	}
}
