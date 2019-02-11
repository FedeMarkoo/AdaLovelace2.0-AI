package Objetos;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.joda.time.*;

import fecha.FechaAhora;
import fecha.FechaCompleta;
import fecha.FechaDentroDe;
import fecha.FechaDesde;
import fecha.FechaDia;
import fecha.FechaGenerico;
import fecha.FechaGet;
import fecha.FechaHaceQue;
import fecha.FechaHasta;
import fecha.FechaHora;

/**
 * Resolvedor, devuelve operaciones relacionadas a la fecha.
 * 
 * dias hasta dias desde hora semana fecha actual
 */
public class Fecha extends Objeto {
	static protected Pattern patternFechaCompleta = Pattern.compile(".*([0-9][0-9])/([0-9]+)/([0-9]+).*");
	static protected Pattern patternDias = Pattern.compile(".* ([0-9]+) ([a-z]+).*");
	static protected DateTime fecha = null;
	static protected Date fecha2 = null;
	static private FechaGenerico primero;
	static private String inicial = null;

	/**
	 * Se agrega un constructor para testear. recive s en formato dd/MM/aaaa y hace
	 * las cuentas en base a eso, asume que son las 10:30 de ese dia.
	 * 
	 * @param s
	 */
	@SuppressWarnings("deprecation")
	public Fecha(String s) {
		Matcher regexFechaCompleta = patternFechaCompleta.matcher(s);
		if (regexFechaCompleta.find()) {
			int dia = Integer.parseInt(regexFechaCompleta.group(1)),
					mes = Integer.parseInt(regexFechaCompleta.group(2)),
					ano = Integer.parseInt(regexFechaCompleta.group(3));
			Fecha.fecha = new DateTime(ano, mes, dia, 10, 30, 0, 0);
			Fecha.fecha2 = new Date(ano - 1900, mes - 1, dia, 10, 30);
		}
		Fecha.inicial = s;
	}

	public Fecha() {
		super();

	}

	public static void crearCadena() {
		if (inicial == null) {
			FechaGenerico ahora = new FechaAhora();
			FechaGenerico get = new FechaGet();
			FechaGenerico comp = new FechaCompleta();
			FechaGenerico hora = new FechaHora();
			FechaGenerico falta = new FechaHasta();
			FechaGenerico paso = new FechaDesde();
			FechaGenerico dentro = new FechaDentroDe();
			FechaGenerico hace = new FechaHaceQue();
			FechaGenerico dia = new FechaDia();

			ahora.siguiente(get);
			get.siguiente(comp);
			comp.siguiente(hora);
			hora.siguiente(falta);
			falta.siguiente(paso);
			paso.siguiente(dentro);
			dentro.siguiente(hace);
			hace.siguiente(dia);
			Fecha.primero = ahora;
		} else {
			FechaGenerico ahora = new FechaAhora(Fecha.inicial);
			FechaGenerico get = new FechaGet(Fecha.inicial);
			FechaGenerico comp = new FechaCompleta(Fecha.inicial);
			FechaGenerico hora = new FechaHora(Fecha.inicial);
			FechaGenerico falta = new FechaHasta(Fecha.inicial);
			FechaGenerico paso = new FechaDesde(Fecha.inicial);
			FechaGenerico dentro = new FechaDentroDe(Fecha.inicial);
			FechaGenerico hace = new FechaHaceQue(Fecha.inicial);
			FechaGenerico dia = new FechaDia(Fecha.inicial);

			ahora.siguiente(get);
			get.siguiente(comp);
			comp.siguiente(hora);
			hora.siguiente(falta);
			falta.siguiente(paso);
			paso.siguiente(dentro);
			dentro.siguiente(hace);
			hace.siguiente(dia);
			Fecha.primero = ahora;
		}
	}

	public static String decime(String mensaje) {
		if (Fecha.primero == null)
			crearCadena();
		return Fecha.primero.request(mensaje);
	}

}
