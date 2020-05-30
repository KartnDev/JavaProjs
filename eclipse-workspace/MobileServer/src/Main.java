import com.KartonDCP.Server.MobileSever.SSLMobileServer;
import com.KartonDCP.Server.MobileSever.Server;


public class Main {
    public static void main(final String[] args) throws Exception {
        System.setProperty("javax.net.ssl.trustStore", "karton.store");
        System.setProperty("javax.net.ssl.keyStorePassword", "zxc123");
        System.out.println(System.getProperty("javax.net.ssl.trustStore"));
        Server server = new SSLMobileServer();
        server.startServing();
    }
}