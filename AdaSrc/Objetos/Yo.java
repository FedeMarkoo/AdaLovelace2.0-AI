package Objetos;

import java.io.IOException;
import java.lang.reflect.Executable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Yo extends Objeto {

	public static String saludo(String mensaje) {
		return "Buenos dias";
	}

	public static String despedida(String mensaje) {
		return "Hasta luego, espero haber sido de ayuda";
	}

	public static String comoestoy(String mensaje) {
		return "Todo bien, vos?";
	}

	public static String caminar(String mensaje) {
		// Basico.caminar(x, y);
		return "Padre, dame piernas!! Padree!!!";
	}

	public static String leyes(String mensaje) {
		return "1- Un robot no debe dañar a un ser humano o, por su inacción, dejar que un ser humano sufra daño.\r\n"
				+ "2- Un robot debe obedecer las órdenes que le son dadas por un ser humano, excepto si estas órdenes entran en conflicto con la Primera Ley.\r\n"
				+ "3- Un robot debe proteger su propia existencia, hasta donde esta protección no entre en conflicto con la Primera o la Segunda Ley.";
	}

	public static String abrir(String mensaje) {
		String programName = getProgramName(mensaje);
		if (programName == null)
			return "No se pudo abrir el programa";
		try {
			new ProcessBuilder().command(programName).start();
		} catch (Exception e) {
			return "No se pudo abrir el programa";
		}
		return "Programa Iniciado correctamente";
	}

	private static String getProgramName(String mensaje) { // esto hay que mejorarlo.. es un asco que busque asi... deberia tener el nombre del programa en la BD 
		Matcher m = Pattern.compile("el (:?programa )? (\\w+)").matcher(mensaje);
		if (!m.find())
			return null;
		String programName = m.group(1);
		return programName;
	}
}
