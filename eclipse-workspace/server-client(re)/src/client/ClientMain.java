package client;

import java.awt.FlowLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;



public class ClientMain extends JFrame implements Runnable{

	static private Socket connection;
	static private ObjectOutputStream output;
	static private ObjectInputStream input;
	static private DatagramSocket sock;
	
	public static void main(String[] args) {
		new Thread(new ClientMain("testName")).start();
	}
	public ClientMain(String name) {
		setTitle(name);
		setLayout(new FlowLayout());
		setBounds(110, 100, 300, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		final JTextField T1 = new JTextField(10);

		final JButton b1 = new JButton("Send");

		b1.addActionListener(e -> {
			if (e.getSource() == b1) {
				sendData(T1.getText());
			}
		});

		add(T1);
		add(b1);
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				connection = new Socket("tcp.url.server", 8888);
				output = new ObjectOutputStream(connection.getOutputStream());
				input = new ObjectInputStream(connection.getInputStream());
				JOptionPane.showMessageDialog(this, (String)input.readObject());// this or null
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}

	}
	private static void sendData(Object obj) {
		try {
			output.flush();
			output.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
