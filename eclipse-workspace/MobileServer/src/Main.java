import com.KartonDCP.MobileSever.TcpMobileServer;


public class Main {
    public static void main(final String[] args) throws Exception {
        TcpMobileServer server = new TcpMobileServer();
        server.startServing();
    }
}