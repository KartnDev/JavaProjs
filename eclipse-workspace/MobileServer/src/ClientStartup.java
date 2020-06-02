import com.KartonDCP.SDK.SSLClient;
import com.KartonDCP.SDK.Status.RegStat;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientStartup {

    public static void main(String[] args) throws Exception {

        final char[] password = "passphrase".toCharArray();


        final KeyStore keyStore = KeyStore.getInstance(new File("keystore"), password);

        final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("NewSunX509");
        keyManagerFactory.init(keyStore, password);

        final SSLContext context = SSLContext.getInstance("TLS");//"SSL" "TLS"
        context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        final SSLSocketFactory factory = context.getSocketFactory();


        SSLClient client  = new SSLClient(InetAddress.getByName("127.0.0.1"), 3304, factory);

        client.randomRegisterAsync().thenAccept((resultStatus) -> {
            System.out.println(resultStatus.getCode() + " | " + resultStatus.getUserToken());
        }).get();



    }
}
