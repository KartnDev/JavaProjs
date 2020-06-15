import com.KartonDCP.SDK.Status.DialogRegResult;
import com.KartonDCP.SDK.Status.RegStatusCode;
import com.KartonDCP.SDK.TcpClient;

import javax.net.SocketFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public class ClientStartup {

    public static void main(String[] args) throws Exception {


        SocketFactory factory = SocketFactory.getDefault();


        TcpClient client  = new TcpClient(InetAddress.getByName("127.0.0.1"), 3304);

        client.randomRegisterAsync().thenAccept((resultStatus) -> {
            System.out.println(resultStatus.getCode() + " | " + resultStatus.getUserToken());

            if(resultStatus.getCode().equals(RegStatusCode.OK)){
                try {
                    TcpClient c  = new TcpClient(InetAddress.getByName("127.0.0.1"), 3304);
                    DialogRegResult r =  c.createDialog(resultStatus.getUserToken(),
                            UUID.fromString("7c328983-6e76-4ab7-b1b5-94f321e3fa60"));

                    System.out.println(r.getStatusCode());
                    System.out.println(r.getDialogToken());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }


            }



        }).get();





    }
}
