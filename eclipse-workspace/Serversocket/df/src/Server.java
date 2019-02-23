package src;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Server implements Runnable {

	private static Socket connection;
	private static ObjectOutputStream output;
	private static ObjectInputStream input;
	private static ServerSocket server;

	public static void main(String[] args) {

	}

	@Override
	public void run() {
		try {
			server = new ServerSocket(7777, 10);
			while (true) {
				connection = server.accept();
				output = new ObjectOutputStream(connection.getOutputStream());
				input = new ObjectInputStream(connection.getInputStream());
				output.writeObject("msg" + (String) input.readObject());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
