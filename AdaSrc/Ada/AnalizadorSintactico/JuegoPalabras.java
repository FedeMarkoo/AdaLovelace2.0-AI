package Ada.AnalizadorSintactico;

import java.util.ArrayList;
import java.util.Collection;

public class JuegoPalabras {
	private ArrayList<String> sustantivos = new ArrayList<>();
	private ArrayList<String> verbos = new ArrayList<>();
	private ArrayList<String> adjetivo = new ArrayList<>();

	public ArrayList<String> getSustantivos() {
		return sustantivos;
	}

	public void addSustantivos(Collection<String> sustantivos) {
		this.sustantivos.addAll(sustantivos);
	}

	public ArrayList<String> getVerbo() {
		return verbos;
	}

	public void addVerbo(Collection<String> verbo) {
		this.verbos.addAll(verbo);
	}

	public ArrayList<String> getAdjetivo() {
		return adjetivo;
	}

	public void addAdjetivo(Collection<String> adjetivo) {
		this.adjetivo.addAll(adjetivo);
	}

	public void addAdjetivo(String adjetivo) {
		this.adjetivo.add(adjetivo);
	}

	public void addVerbo(String verbo) {
		this.verbos.add(verbo);
	}

	public void addSustantivo(String sustantivo) {
		this.sustantivos.add(sustantivo);
	}

}
