package testeo;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Manager {

	public JFrame frame;
	private DataOutputStream bufferDeSalida;
	public static JTextField escucha;
	public static JTextArea dice;

	/**
	 * Create the application.
	 */
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
		try {
			bufferDeSalida.writeUTF(text);
		} catch (IOException e) {
		}
	}

	private void conectar() {
		ServerSocket serversock = null;

		while (true)
			try {
				serversock = new ServerSocket(5050);
				Socket socket = serversock.accept();

				DataOutputStream bufferDeSalida = new DataOutputStream(socket.getOutputStream());
				DataInputStream bufferDeEntrada = new DataInputStream(socket.getInputStream());

				setBufferSalida(bufferDeSalida);

				Thread recibir = new Thread() {
					public void run() {
						while (true)
							try {
								dice.setText(bufferDeEntrada.readUTF());
							} catch (IOException e) {
							}
					}
				};

				recibir.start();

				while (true)
					;

			} catch (Exception e) {
				System.out.println("Error en socket");
				try {
					serversock.close();
				} catch (Exception e1) {
				}
			}

	}

	private void setBufferSalida(DataOutputStream bufferDeSalida) {
		this.bufferDeSalida = bufferDeSalida;
	}

}
