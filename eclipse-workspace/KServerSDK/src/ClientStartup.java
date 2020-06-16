import com.KartonDCP.SDK.Models.DialogEntity;
import com.KartonDCP.SDK.Status.DialogRegResult;
import com.KartonDCP.SDK.Status.SendStatus;
import com.KartonDCP.SDK.TcpClient;

import javax.net.SocketFactory;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class ClientStartup {

    public static void main(String[] args) throws Exception {


        SocketFactory factory = SocketFactory.getDefault();


        TcpClient client = new TcpClient(InetAddress.getByName("127.0.0.1"), 3304);

        Collection<DialogEntity> result =  client.getDialogs(UUID.fromString("47a71c49-7d27-4938-9c43-82c37cd3f9c0"));

        
    }
}
