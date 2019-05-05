package BaseDeDatos;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import Ada.Acciones.Basico;
import Ada.AnalizadorSintactico.AnalizadorSintactico;
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
			System.out.println("Se envia " + parametros);
			bufferSalida.writeObject(parametros);
		} catch (Exception e) {
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

	private static Tipo decodificarPorFrase(String texto) {
		Tipo comboSintactico = AnalizadorSintactico.analizar(texto);
		if (comboSintactico == null)
			if (ingresarCombo(texto))
				return decodificarPorFrase(texto);
			else
				Basico.decir("No se reconoce la oracion");
		return comboSintactico;
	}

	public static Tipo decodificar(String texto) {
		Tipo deco = decodificarPorFrase(texto);
		return deco;
	}

	private static boolean ingresarCombo(String texto) {
		Basico.decir("Desea ingresar el tipo de oracion?");
		String escuchar = Basico.escuchar();
		if (escuchar.contains("s")) {
			Basico.decir("Por favor, ingrese como se compone la oracion.");
			Basico.decir("Por ejemplo, Sustantivo verbo sustantico adjetivo");
			Basico.decir("Puede usarse el caracter ? para indicar que una palabra es opcional");
			String combo = Basico.escuchar();
			if (cargarCombo(combo)) {
				Basico.decir("Carga realizada con exito.");
				return false;
			} else
				Basico.decir("Fallo al cargar la compocicion sintactica");
		}
		return false;
	}

	public static String[] tipoSintactico(String palabra) {
		enviarComando("tipoSintactico");
		enviarComando(1);
		enviarComando(palabra);
		return (String[]) recibirComando();
	}

	public static boolean cargarCombo(String cad) {
		enviarComando("cargarCombo");
		enviarComando(1);
		enviarComando(cad);
		return (boolean) recibirComando();
	}

	public static String getTipo(String cad) {
		enviarComando("getTipo");
		enviarComando(1);
		enviarComando(cad);
		return (String) recibirComando();
	}

	public static String getSinonimoVerbo(String cad) {
		enviarComando("getSinonimoVerbo");
		enviarComando(1);
		enviarComando(cad);
		return (String) recibirComando();
	}

	public static String getSinonimoObjeto(String cad) {
		enviarComando("getSinonimoObjeto");
		enviarComando(1);
		enviarComando(cad);
		return (String) recibirComando();
	}

	public static boolean ingresarPalabra(Palabra palabra) {
		enviarComando("ingresarPalabra");
		enviarComando(1);
		enviarComando(palabra);
		return (boolean) recibirComando();
	}

	public static boolean ingresarTipo(String palabra, String tipo) {
		enviarComando("ingresarTipo");
		enviarComando(2);
		enviarComando(palabra);
		enviarComando(tipo);
		return (boolean) recibirComando();
	}

	public static boolean cargarSinonimoSustantivo(String objeto, String sinonimo) {
		enviarComando("cargarSinonimoSustantivo");
		enviarComando(2);
		enviarComando(objeto);
		enviarComando(sinonimo);
		return (boolean) recibirComando();
	}

	public static boolean cargarSinonimoVerbo(String accion, String sinonimo) {
		enviarComando("cargarSinonimoVerbo");
		enviarComando(2);
		enviarComando(accion);
		enviarComando(sinonimo);
		return (boolean) recibirComando();
	}

	public static boolean crearMetodo(String accion) {
		enviarComando("crearMetodo");
		enviarComando(1);
		enviarComando(accion);
		return (boolean) recibirComando();
	}

	public static boolean crearClase(String clase) {
		enviarComando("crearClase");
		enviarComando(1);
		enviarComando(clase);
		return (Boolean) recibirComando();
	}

	public static String getTipoDato(String atributo) {
		enviarComando("getTipoDato");
		enviarComando(1);
		enviarComando(atributo);
		return (String) recibirComando();
	}

	@SuppressWarnings("unchecked")
	public static List<String> getCombinacionesSintactico() {
		enviarComando("getCombinacionesSintactico");
		enviarComando(0);
		return (List<String>) recibirComando();
	}

	public static String getUltimaModificacion() {
		enviarComando("getUltimaModificacion");
		enviarComando(0);
		return (String) recibirComando();
	}

	@SuppressWarnings("unchecked")
	public static List<MapeoClase> getClases() {
		enviarComando("getClases");
		enviarComando(0);
		return (List<MapeoClase>) recibirComando();
	}

	public static boolean ingresarClase(MapeoClase mapeoClase) {
		enviarComando("ingresarClase");
		enviarComando(1);
		enviarComando(mapeoClase);
		return (boolean) recibirComando();
	}
}
