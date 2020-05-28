package SDK;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class SSLClient implements Runnable{

    private SSLSocket innerSock;

    @SuppressWarnings("SpellCheckingInspection")
    public static final String appToken = "98F1EJJDa4fjwD2fUIHWUd2dsaAsS289IFFFadde3A8213HFI7";


    public SSLClient(InetAddress endPoint, int port, SSLSocketFactory factory){
        try {
            innerSock = (SSLSocket) factory.createSocket(endPoint, port);
        } catch (IOException e) {
            System.err.println(e);
        }

    }

    private String formatTheRequest(String methodName, Map<String, String> args) {
        StringBuffer sb = new StringBuffer();
        int iterator = 0;

        args.forEach((key, value) -> { // я че в джаваскрипт попал???
            sb.append(key);
            sb.append("=");
            sb.append(value);
            if(iterator != args.size() - 1){
                sb.append("&");
            }
        });

        return String.format("%s?{%s}?%s", appToken, methodName, sb.toString());
    }


    private String getRandWord(int targetStringLength) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    private String createRandUser(){
        var args = new HashMap<String, String>();

        args.put("name", getRandWord(6));
        args.put("surname", getRandWord(10));
        args.put("password", getRandWord(3) +
                UUID.randomUUID().toString() +
                getRandWord(3));

        var rand = new Random();
        args.put("phone_num", "8" + rand.nextInt(999999999) + rand.ints(10));


    }



    @Override
    public void run() {
        try {
            innerSock.startHandshake();
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(innerSock.getOutputStream())));




            out.println();
            out.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(innerSock.getInputStream()));
            in.read();

        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
