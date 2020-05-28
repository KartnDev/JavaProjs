import com.KartonDCP.MobileSever.SSLMobileServer;
import com.KartonDCP.MobileSever.Server;
import com.KartonDCP.MobileSever.TcpMobileServer;


public class Main {
    public static void main(final String[] args) throws Exception {
        Server server = new SSLMobileServer();
        server.startServing();
    }
}