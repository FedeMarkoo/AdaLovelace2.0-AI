package Ada.AnalizadorSintactico;

import java.util.ArrayList;

public class Tipo {

	private boolean match;
	private JuegoPalabras juego;

	public Tipo(String combinacion, String mensaje) {
		juego = new JuegoPalabras();
		ArrayList<Palabra> palabraTemp = new ArrayList<>();
		for (String palabra : mensaje.split(" ")) {
			palabraTemp.add(new Palabra(palabra));
		}

		Palabra[] palabras = (Palabra[]) palabraTemp.toArray();
		String[] tipos = combinacion.split(" ");
		int indice = 0;
		int desface = 0;
		int largoCombo = tipos.length, largoPalabra = palabras.length;
		// el desface es por los adverbios ya que no afentan al sentido de la oracion
		while (indice < largoCombo && indice + desface < largoPalabra) {
			Palabra palabra = palabras[indice + desface];
			String tipo = tipos[indice];
			if (!palabra.match(tipo))
				if (palabra.isAdverbio() || palabra.isSigno())
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
					juego.addSustantivo(tipo);
					break;
				case "adjetivo":
					juego.addAdjetivo(tipo);
					break;
				case "verbo":
					juego.addVerbo(tipo);
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
		return getJuegoPalabras().getSustantivos().get(0);
	}

	public String metodo() {
		return getJuegoPalabras().getVerbo().get(0);
	}
}
