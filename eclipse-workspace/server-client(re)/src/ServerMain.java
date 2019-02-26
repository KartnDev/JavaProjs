import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

	private static Socket connection;
	private static ServerSocket server;
	private static ObjectInputStream inputStream;
	private static ObjectOutputStream outputStream;
	private static Thread thread;

	
	
	public static void main(String[] args) {

		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					server = new ServerSocket(8888, 10, InetAddress.getByName("tcp.url.server"));
					while (true) {
						connection = server.accept();
						outputStream = new ObjectOutputStream(connection.getOutputStream());
						inputStream = new ObjectInputStream(connection.getInputStream());
						outputStream.writeObject("msg: " + (String) inputStream.readObject());
					}
				} catch (Exception ex) {
				}
			}

		});

		thread.start();

	}
}
