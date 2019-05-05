
package Manager;

import java.awt.Color;
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
import java.util.Hashtable;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import Ada.AnalizadorSintactico.Palabra;
import BaseDeDatos.BD;
import BaseDeDatos.BDAdaManager;
import BaseDeDatos.MapeoClase;

public class Manager {

	private JFrame frame;
	private String anterior = "";
	private static DataOutputStream bufferSalida;
	private static DataInputStream bufferEntrada;
	private static JTextField escucha;
	private static JPanel dice;
	public static boolean adaManagerThreadIsRunning = false;
	public static Thread adaManagerThread = new Thread() {

		public void run() {
			adaManagerThreadIsRunning = true;
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
			adaManagerThreadIsRunning = false;
		}
	};

	private static Thread bdAdaManger = new Thread() {
		public void run() {
			Class<BD> claseBD = BD.class;
			Hashtable<String, Method> metodos = new Hashtable<>();
			for (Method temp : claseBD.getMethods())
				metodos.put(temp.getName(), temp);

			while (true)
				try {
					Method method = null;
					String methodName = (String) BDAdaManager.recibirComando();
					int cantidad = (int) BDAdaManager.recibirComando();
					int offset = cantidad;
					Object[] parametro = new Object[cantidad];
					while (offset-- > 0)
						parametro[offset] = BDAdaManager.recibirComando();

					method = metodos.get(methodName);

					Object retorno = null;
					switch (cantidad) {
					case 0:
						retorno = method.invoke(0);
						break;
					case 1:
						retorno = method.invoke(1, parametro[0]);
						break;
					case 2:
						retorno = method.invoke(2, parametro[0], parametro[1]);
						break;
					}
					BDAdaManager.enviarComando(retorno);
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	};
	private JScrollPane scrollBar;

	/**
	 * Create the application.
	 */

	public static void main(String[] a) {
		System.out.println("main");
		new Manager();
	}

	public Manager() {
		System.out.println("Manager");
		BD.conectar();
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

		conectar();

		escucha = new JTextField();
		bdAdaManger.start();

		dice = new JPanel();
		dice.setBackground(Color.WHITE);
		dice.add(Mensaje.nuevo(""), Mensaje.gbc(), dice.getComponentCount());
		scrollBar = new JScrollPane(dice);
		GridBagConstraints gbc_scrollBar = new GridBagConstraints();
		gbc_scrollBar.fill = GridBagConstraints.BOTH;
		gbc_scrollBar.insets = new Insets(0, 0, 5, 0);
		gbc_scrollBar.gridx = 0;
		gbc_scrollBar.gridy = 0;
		gbc_scrollBar.weightx = 1;
		gbc_scrollBar.weighty = 1;
		scrollBar.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollBar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		frame.getContentPane().add(scrollBar, gbc_scrollBar);

		GridBagLayout gbl_mensajes = new GridBagLayout();
		gbl_mensajes.columnWidths = new int[] { 0 };
		gbl_mensajes.rowHeights = new int[] { 0 };
		gbl_mensajes.columnWeights = new double[] { Double.MIN_VALUE };
		gbl_mensajes.rowWeights = new double[] { Double.MIN_VALUE };
		dice.setLayout(gbl_mensajes);

		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 1;
		frame.getContentPane().add(escucha, gbc_textField);

		escucha.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyCode() != KeyEvent.VK_ENTER)
					return;
				String mensaje = escucha.getText();
				enviar(mensaje);
				mensaje = (anterior.equals("Usted") ? "" : "Usted:\n") + mensaje;
				dice.add(Mensaje.nuevo(mensaje), Mensaje.gbc(), dice.getComponentCount());
				anterior = "Usted";
				escucha.setText("");
				dice.revalidate();
				dice.repaint();
				JScrollBar verticalScrollBar = scrollBar.getVerticalScrollBar();
				int maximum = verticalScrollBar.getMaximum();
				verticalScrollBar.setValue(maximum);
				verticalScrollBar.setValue(maximum + 1);
				verticalScrollBar.setValue(Integer.MAX_VALUE);
			}
		});
		escucha.setEnabled(false);
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
			@SuppressWarnings("resource")
			public void run() {
				ServerSocket serversock = null;
				try {
					serversock = new ServerSocket(5050);
				} catch (Exception e2) {
				}
				Socket socket = null;
				while (true)
					try {
						System.out.println("Conectando Socket");
						adaManager();
						socket = serversock.accept();
						System.out.println("Socket conectado");
						DataOutputStream bufferDeSalida = new DataOutputStream(socket.getOutputStream());
						DataInputStream bufferDeEntrada = new DataInputStream(socket.getInputStream());
						setBuffers(bufferDeSalida, bufferDeEntrada);
						escucha.setEnabled(true);
						while (true) {
							String readUTF = bufferEntrada.readUTF();
							if (readUTF.contains("Clase-Actualizada-RECOMPILAR-COD:92929"))
								adaManagerThread.start();

							else {
								String mensaje = (anterior.equals("Ada") ? "" : "Ada:\n") + readUTF;
								dice.add(Mensaje.nuevo(mensaje), Mensaje.gbc(), dice.getComponentCount());
								anterior = "Ada";
								dice.revalidate();
								dice.repaint();
								JScrollBar verticalScrollBar = scrollBar.getVerticalScrollBar();
								int maximum = verticalScrollBar.getMaximum();
								verticalScrollBar.setValue(maximum);
								verticalScrollBar.setValue(maximum + 1);
								verticalScrollBar.setValue(Integer.MAX_VALUE);
							}
						}
					} catch (Exception e) {
						try {
							System.out.println("Error en socket");
							escucha.setEnabled(false);
							socket.close();
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
		if (!adaManagerThreadIsRunning) {
			adaManagerThreadIsRunning = true;
			System.out.println("run");
			Date modificado = new Date(0);
			// String last = BD.getUltimaModificacion();
			String last = "2007-12-03T10:15:30.00Z";
			// 2007-12-03T10:15:30.00Z.
			Date ultimo = Date.from(Instant.parse(last));
			if (modificado.before(ultimo)) {
				modificado = ultimo;
				recompilar();
			}
			adaManagerThreadIsRunning = false;
		}
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
