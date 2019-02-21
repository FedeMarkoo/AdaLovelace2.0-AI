package BaseDeDatos;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import Ada.AnalizadorSintactico.Palabra;
import Ada.AnalizadorSintactico.Tipo;

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
			InputStream inputStream = null;
			try {
				inputStream = socket.getInputStream();
			} catch (Exception e1) {
			}
			try {
				bufferSalida = new ObjectOutputStream(socket.getOutputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				bufferEntrada = new ObjectInputStream(inputStream);
			} catch (IOException e1) {
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
			enviarComando(parametros);
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

	public static Tipo decodificar(String texto) {
		enviarComando("decodificar");
		enviarComando(texto);
		return (Tipo) recibirComando();
	}

	public static String getTipo(String cad) {
		enviarComando("getTipo");
		enviarComando(cad);
		return (String) recibirComando();
	}

	public static String getSinonimoVerbo(String cad) {
		enviarComando("getSinonimoVerbo");
		enviarComando(cad);
		return (String) recibirComando();
	}

	public static String getSinonimoObjeto(String cad) {
		enviarComando("getSinonimoVerbo");
		enviarComando(cad);
		return (String) recibirComando();
	}

	public static boolean ingresarPalabra(Palabra palabra) {
		enviarComando("ingresarPalabra");
		enviarComando(palabra);
		return (boolean) recibirComando();
	}

	public static boolean ingresarTipo(String palabra, String tipo) {
		String[] param = new String[] { palabra, tipo };
		enviarComando("ingresarTipo");
		enviarComando(param);
		return (boolean) recibirComando();
	}

	public static boolean cargarSinonimoSustantivo(String objeto, String sinonimo) {
		String[] param = new String[] { objeto, sinonimo };
		enviarComando("cargarSinonimoSustantivo");
		enviarComando(param);
		return (boolean) recibirComando();
	}

	public static boolean cargarSinonimoVerbo(String accion, String sinonimo) {
		String[] param = new String[] { accion, sinonimo };
		enviarComando("cargarSinonimoVerbo");
		enviarComando(param);
		return (boolean) recibirComando();
	}

	public static boolean crearMetodo(String accion) {
		enviarComando("crearMetodo");
		enviarComando(accion);
		return (boolean) recibirComando();
	}

	public static boolean crearClase(String clase) {
		enviarComando("crearClase");
		enviarComando(clase);
		return (Boolean) recibirComando();
	}

	public static String getTipoDato(String atributo) {
		enviarComando("getTipoDato");
		enviarComando(atributo);
		return (String) recibirComando();
	}

	@SuppressWarnings("unchecked")
	public static List<String> getCombinacionesSintactico() {
		enviarComando("getCombinacionesSintactico");
		enviarComando(null);
		return (List<String>) recibirComando();
	}

	public static String getUltimaModificacion() {
		enviarComando("getUltimaModificacion");
		enviarComando(null);
		return (String) recibirComando();
	}

	@SuppressWarnings("unchecked")
	public static List<MapeoClase> getClases() {
		enviarComando("getClases");
		enviarComando(null);
		return (List<MapeoClase>) recibirComando();
	}

	public static boolean ingresarClase(MapeoClase mapeoClase) {
		enviarComando("ingresarClase");
		enviarComando(mapeoClase);
		return (boolean) recibirComando();
	}

	public static String[] tipoSintactico(String palabra) {
		enviarComando("tipoSintactico");
		enviarComando(palabra);
		return (String[]) recibirComando();
	}
}
