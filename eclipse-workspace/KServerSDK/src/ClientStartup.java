import com.KartonDCP.SDK.Status.SendStatus;
import com.KartonDCP.SDK.TcpClient;

import javax.net.SocketFactory;
import java.net.InetAddress;
import java.util.UUID;

public class ClientStartup {

    public static void main(String[] args) throws Exception {


        SocketFactory factory = SocketFactory.getDefault();


        TcpClient client = new TcpClient(InetAddress.getByName("127.0.0.1"), 3304);

        SendStatus s = client.sendMessage("HI",
                UUID.fromString("47a71c49-7d27-4938-9c43-82c37cd3f9c0"),
                UUID.fromString("cd43d3aa-cd39-4a3b-9518-1a94a237eda3"));

        System.out.println(s.toString());
    }
}
