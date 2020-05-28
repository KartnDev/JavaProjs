import com.KartonDCP.Server.MobileSever.SSLMobileServer;
import com.KartonDCP.Server.MobileSever.Server;


public class Main {
    public static void main(final String[] args) throws Exception {
        Server server = new SSLMobileServer();
        server.startServing();
    }
}