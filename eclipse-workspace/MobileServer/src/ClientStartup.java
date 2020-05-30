import com.KartonDCP.SDK.SSLClient;

import javax.net.ssl.SSLSocketFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientStartup {

    public static void main(String[] args) throws UnknownHostException {

        System.setProperty("javax.net.ssl.trustStore", "karton.store");
        System.setProperty("javax.net.ssl.keyStorePassword", "zxc123");
        var factory = SSLSocketFactory.getDefault();


        ExecutorService pool = Executors.newFixedThreadPool(3);


        pool.submit(new SSLClient(InetAddress.getByName("127.0.0.1"), 3305, factory));


        pool.shutdown();

    }
}
