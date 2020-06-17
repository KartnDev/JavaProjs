import com.KartonDCP.SDK.Models.MessageEntity;
import com.KartonDCP.SDK.TcpClient;
import com.KartonDCP.Utils.Random.RandomWork;

import javax.net.SocketFactory;
import java.net.InetAddress;
import java.util.Collection;
import java.util.UUID;

public class ClientStartup {

    public static void main(String[] args) throws Exception {


        SocketFactory factory = SocketFactory.getDefault();


        //Collection<DialogEntity> result =  client.getDialogs(UUID.fromString("47a71c49-7d27-4938-9c43-82c37cd3f9c0"));


        TcpClient client = new TcpClient(InetAddress.getByName("127.0.0.1"), 3304);
        //client.sendMessage(RandomWork.getRandWord(10), UUID.fromString("ff04cfb4-cbf3-4601-be81-a8134d674054"), UUID.fromString("cd43d3aa-cd39-4a3b-9518-1a94a237eda3"));


        Collection<MessageEntity> query = client.getMessages(UUID.fromString("cd43d3aa-cd39-4a3b-9518-1a94a237eda3"));


        for (MessageEntity msg : query) {
            System.out.println(msg.getFrom());
            System.out.println(msg.getTo());
            System.out.println(msg.getMessageBody());
        }


    }
}
