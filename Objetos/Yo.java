package Objetos;

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
	
	public static String leyes(String mensaje) {
		return "1- Un robot no debe da�ar a un ser humano o, por su inacci�n, dejar que un ser humano sufra da�o.\r\n"
				+ "2- Un robot debe obedecer las �rdenes que le son dadas por un ser humano, excepto si estas �rdenes entran en conflicto con la Primera Ley.\r\n"
				+ "3- Un robot debe proteger su propia existencia, hasta donde esta protecci�n no entre en conflicto con la Primera o la Segunda Ley.";
	}
}