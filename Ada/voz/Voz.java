package Ada.voz;

import java.util.ArrayList;
import java.util.Locale;

import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Voice;

public class Voz {

	private static ArrayList<String> lista = new ArrayList<>();
	private static Thread hilo = new Thread() {
		public void run() {
			while (true) {
				while (lista.isEmpty())
					try {
						sleep(200);
					} catch (Exception e) {
					}
				decir(lista.remove(0));
			}
		}
	};

	public static void speak(String texto) {
		if (!hilo.isAlive())
			hilo.start();
		lista.add(texto);
	}

	private static void decir(String texto) {
		try {
//			Runtime.getRuntime().exec("espeak -ves-la \"" + texto + "\"").waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void speakFalla(String text) {

		if (text == null || text.trim().isEmpty())
			return;

		String voiceName = "kevin16";

		try {
			System.setProperty("FreeTTSSynthEngineCentral", "com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
			System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
			Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");

			SynthesizerModeDesc desc = new SynthesizerModeDesc(null, "general", Locale.US, null, null);

			Synthesizer synth = Central.createSynthesizer(desc);
			synth.allocate();
			desc = (SynthesizerModeDesc) synth.getEngineModeDesc();
			Voice[] voices = desc.getVoices();
			Voice voice = null;
			for (Voice entry : voices) {
				if (entry.getName().equals(voiceName)) {
					voice = entry;
					break;
				}
			}
			synth.getSynthesizerProperties().setVoice(voice);
			synth.resume();
			synth.speakPlainText(text, null);
			synth.waitEngineState(Synthesizer.QUEUE_EMPTY);
			synth.deallocate();

		} catch (Exception ex) {
			String message = " missing speech.properties in " + System.getProperty("user.home") + "\n";
			System.out.println("" + ex);
			System.out.println(message);
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Voz.speak("Buenos dias, soy ada lovelace y ando para el culo y hablo como traba");
	}
}
