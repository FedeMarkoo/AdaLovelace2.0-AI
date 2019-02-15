package Manager;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import BaseDeDatos.BD;
import BaseDeDatos.BDAda;
import BaseDeDatos.MapeoClase;

public class Manager {

	private JFrame frame;
	private String anterior = "";
	private static DataOutputStream bufferSalida;
	private static DataInputStream bufferEntrada;
	private static JTextField escucha;
	private static JTextArea dice;
	public static Thread adaManagerThread = new Thread() {
		public void run() {
			System.out.println("run");
			this.setName("AdaManagerThread");
			Date modificado = new Date(0);
			// String last = BD.getUltimaModificacion();
			String last = "2007-12-03T10:15:30.00Z";
			// 2007-12-03T10:15:30.00Z.
			Date ultimo = Date.from(Instant.parse(last));
			if (modificado.before(ultimo)) {
				modificado = ultimo;
				recompilar();
			}
		}
	};

	private static Thread bdAdaManger = new Thread() {
		public void run() {
			BDAda bdAda = new BDAda();
			try {
				Method method = BD.class.getMethod((String) bdAda.recibirComando());
				Object invoke = method.invoke(1, bdAda.recibirComando());
				bdAda.enviarComando(invoke);
			} catch (Exception e) {
			}
		}
	};

	/**
	 * Create the application.
	 */

	public static void main(String[] a) {
		System.out.println("main");
		new Manager();
	}

	public Manager() {
		System.out.println("Manager");
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		System.out.println("initialize");
		frame = new JFrame();
		frame.setBounds(100, 100, 343, 393);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		dice = new JTextArea();
		dice.setEditable(false);
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.insets = new Insets(0, 0, 5, 0);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 0;
		frame.getContentPane().add(dice, gbc_textArea);

		conectar();

		escucha = new JTextField();
		escucha.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyCode() != KeyEvent.VK_ENTER)
					return;
				String text = escucha.getText();
				enviar(text);
				dice.append((anterior.equals("Usted") ? "\n" : "\n\nUsted:\n") + text);
				anterior = "Usted";
				escucha.setText("");
			}
		});
		escucha.setEnabled(false);

		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 1;
		frame.getContentPane().add(escucha, gbc_textField);
		escucha.setColumns(10);
	}

	private void enviar(String text) {
		if (text.trim().length() == 0 || bufferSalida == null)
			return;
		try {
			System.out.println("enviar");
			bufferSalida.writeUTF(text.trim());
			System.out.println("enviado");
		} catch (IOException e) {
		}
	}

	private void conectar() {
		System.out.println("conectar");
		new Thread() {
			public void run() {
				ServerSocket serversock = null;
				try {
					serversock = new ServerSocket(5050);
				} catch (Exception e2) {
				}
				while (true)
					try {
						System.out.println("Conectando Socket");
						adaManager();
						bdAdaManger.start();
						Socket socket = serversock.accept();
						System.out.println("Socket conectado");
						DataOutputStream bufferDeSalida = new DataOutputStream(socket.getOutputStream());
						DataInputStream bufferDeEntrada = new DataInputStream(socket.getInputStream());
						setBuffers(bufferDeSalida, bufferDeEntrada);
						escucha.setEnabled(true);
						try {
							while (true) {
								String readUTF = bufferEntrada.readUTF();
								if (readUTF.contains("Clase-Actualizada-RECOMPILAR-COD:92929"))
									adaManagerThread.start();

								else {
									dice.append((anterior.equals("Ada") ? "\n" : "\n\nAda:\n") + readUTF);
									anterior = "Ada";
								}

							}
						} catch (Exception e) {
						}

					} catch (Exception e) {
						System.out.println("Error en socket");
						e.printStackTrace();
						escucha.setEnabled(false);
						try {
							serversock.close();
						} catch (Exception e1) {
						}
					}
			}
		}.start();
	}

	private static void setBuffers(DataOutputStream bufferDeSalida, DataInputStream bufferDeEntrada) {
		System.out.println("setBuffers");
		bufferSalida = bufferDeSalida;
		bufferEntrada = bufferDeEntrada;
	}

	private void adaManager() {
		System.out.println("adaManager");
		if (!adaManagerThread.isAlive())
			adaManagerThread.start();
	}

	private static Process recompilar() {
		System.out.println("recompilar");
		String path = System.getProperty("user.dir");
		int fin = path.indexOf("AdaLovelace2.0-AI");
		path = path.substring(0, fin);
		path += "AdaLovelace2.0-AI\\";

		// escribirClasesDesdeBD(path);

		try {
			System.out.println("Corriendo ANT");
			Runtime.getRuntime().exec("cmd /c ant -f Manager/build.xml compile,jar,run");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void escribirClasesDesdeBD(String path) {
		List<MapeoClase> clases = BD.getClases();
		for (MapeoClase clase : clases) {
			if (!clase.getNombre().contains("Manager"))
				try {
					String nombre = path.substring(0, path.length() - 1);
					String parte = clase.getNombre().replace("AdaLovelace2.0-AI.", "");
					String[] split = parte.split("\\.");
					for (String cad : split)
						nombre += "\\" + cad;

					nombre = nombre.replace("\\java", ".java").replace("\\sql", ".sql")
							.replace("\\cfg\\xml", ".cfg.xml").replace("\\hbm\\xml", ".hbm.xml");
					BufferedWriter f = new BufferedWriter(new FileWriter(nombre));
					f.write(clase.getCodigo());
					f.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

}
