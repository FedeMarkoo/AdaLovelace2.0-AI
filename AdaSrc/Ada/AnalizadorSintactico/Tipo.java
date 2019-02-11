package Ada.AnalizadorSintactico;

import java.util.ArrayList;

import BaseDeDatos.BD;

public class Tipo {

	private boolean match;
	private JuegoPalabras juego;

	public Tipo(String combinacion, String mensaje) {
		juego = new JuegoPalabras();
		ArrayList<Palabra> palabraTemp = new ArrayList<>();
		for (String palabra : mensaje.split(" ")) {
			palabraTemp.add(new Palabra(palabra));
		}

		Palabra[] palabras = (Palabra[]) palabraTemp.toArray(new Palabra[0]);
		String[] tipos = combinacion.split(" ");
		int indice = 0;
		int desface = 0;
		int largoCombo = tipos.length, largoPalabra = palabras.length;
		// el desface es por los adverbios ya que no afentan al sentido de la oracion
		while (indice < largoCombo && indice + desface < largoPalabra) {
			Palabra palabra = palabras[indice + desface];
			String tipo = tipos[indice];
			if (!palabra.match(tipo))
				if (palabra.isAdverbio() || palabra.isSigno() || palabra.ignorar())
					desface++;
				else if (tipo.contains("?")) {
					desface--;
					indice++;
				} else {
					match = false;
					return;
				}
			else {
				indice++;
				switch (tipo) {
				case "sustantivo":
					juego.addSustantivo(palabra.palabra);
					break;
				case "adjetivo":
					juego.addAdjetivo(palabra.palabra);
					break;
				case "interjección":
				case "verbo":
					juego.addVerbo(palabra.palabra);
					break;
				}
			}
		}
		match = true;
	}

	public boolean match() {
		return match;
	}

	public JuegoPalabras getJuegoPalabras() {
		return juego;
	}

	public String clase() {
		ArrayList<String> sustantivos = getJuegoPalabras().getSustantivos();
		if(sustantivos.isEmpty())
			return "yo";
		return BD.getSinonimoObjeto(sustantivos.get(0));
	}

	public String metodo() {
		return BD.getSinonimoVerbo(getJuegoPalabras().getVerbo().get(0));
	}
}
