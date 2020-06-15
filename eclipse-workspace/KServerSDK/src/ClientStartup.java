import com.KartonDCP.SDK.TcpClient;

import javax.net.SocketFactory;
import java.net.InetAddress;

public class ClientStartup {

    public static void main(String[] args) throws Exception {


        SocketFactory factory = SocketFactory.getDefault();

        for (int i = 0; i < 100; i++) {
            TcpClient client  = new TcpClient(InetAddress.getByName("127.0.0.1"), 3304, factory);

            client.randomRegisterAsync().thenAccept((resultStatus) -> {
                System.out.println(resultStatus.getCode() + " | " + resultStatus.getUserToken());
            }).get();
        }




    }
}
