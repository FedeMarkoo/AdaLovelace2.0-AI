package testeo;

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
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import BaseDeDatos.BD;
import BaseDeDatos.MapeoClase;

public class Manager {

	private JFrame frame;
	private DataOutputStream bufferSalida;
	private DataInputStream bufferEntrada;
	private static JTextField escucha;
	private static JTextArea dice;

	/**
	 * Create the application.
	 */

	public static void main(String[] a) {
		new Manager();
	}

	public Manager() {
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
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
				escucha.setText("");
			}
		});

		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 1;
		frame.getContentPane().add(escucha, gbc_textField);
		escucha.setColumns(10);
	}

	private void enviar(String text) {
		if (text.trim().length() == 0)
			return;
		try {
			bufferSalida.writeUTF(text.trim());
		} catch (IOException e) {
		}
	}

	private void conectar() {
		new Thread() {
			public void run() {
				ServerSocket serversock = null;

				while (true)
					try {
						serversock = new ServerSocket(5050);
						adaManager();
						Socket socket = serversock.accept();

						DataOutputStream bufferDeSalida = new DataOutputStream(socket.getOutputStream());
						DataInputStream bufferDeEntrada = new DataInputStream(socket.getInputStream());

						setBuffers(bufferDeSalida, bufferDeEntrada);
						System.out.println("server conectado y buffers conectados");

						while (true)
							try {
								dice.setText(bufferEntrada.readUTF());
							} catch (Exception e) {
							}

					} catch (Exception e) {
						System.out.println("Error en socket");
						try {
							serversock.close();
						} catch (Exception e1) {
						}
					}
			}
		}.start();
	}

	private void setBuffers(DataOutputStream bufferDeSalida, DataInputStream bufferDeEntrada) {
		this.bufferSalida = bufferDeSalida;
		this.bufferEntrada = bufferDeEntrada;
	}

	private void adaManager() {
		new Thread() {
			public void run() {
				Process p = null;
				Date modificado = new Date(0);
				while (true) {
					String last = BD.getUltimaModificacion();
					// 2007-12-03T10:15:30.00Z.
					Date ultimo = Date.from(Instant.parse(last));
					if (modificado.before(ultimo)) {
						modificado = ultimo;
						if (p != null)
							p.destroy();
						p = recompilar();
					}
				}
			}
		}.start();
	}

	private Process recompilar() {
		String path = System.getProperty("user.dir");
		int fin = path.indexOf("AdaLovelace2.0-AI");
		path = path.substring(0, fin);
		path += "AdaLovelace2.0-AI\\";
		String pathAda = path + "Ada\\AdaLovelace.java";

		List<MapeoClase> clases = BD.getClases();

		for (MapeoClase clase : clases) {
			try {
				String nombre = path + clase.getNombre().replaceAll("\\.", "\\").replace("\\java", ".java");
				BufferedWriter f = new BufferedWriter(new FileWriter(nombre));
				f.write(clase.getCodigo());
				f.close();
			} catch (Exception e) {
			}
		}

		try {

//			Runtime.getRuntime().exec("javac \"" + pathAda + "\"");
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			if (compiler == null) {
				System.setProperty("java.home", "C:\\Program Files\\Java\\jdk1.8.0_191");
				compiler = ToolProvider.getSystemJavaCompiler();
			}
			compiler.run(null, null, null, pathAda);
		} catch (Exception e) {
		}

		try {
			return Runtime.getRuntime().exec("java -classpath \"C:\\Users\\Fede\\Escritorio\\Proyectos\\AdaLovelace2.0-AI\" Ada.AdaLovelace");
		} catch (Exception e) {
		}
		return null;
	}

}
