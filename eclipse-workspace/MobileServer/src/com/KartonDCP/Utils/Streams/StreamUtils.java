package com.KartonDCP.Utils.Streams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;

public final class StreamUtils {
    public final static CompletableFuture<String> InputStreamToStringAsync(InputStream is){

        CompletableFuture<String> asyncResult = new CompletableFuture<>();

        asyncResult.supplyAsync(() -> {
            BufferedReader bufferedStreamReader = new BufferedReader(new InputStreamReader(is));

            char buff[] = new char[1024];
            try {
                bufferedStreamReader.read(buff);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new String(buff);
        });
        return asyncResult;
    }

    public final static String InputStreamToString(InputStream is) throws IOException {

        BufferedReader bufferedStreamReader = new BufferedReader(new InputStreamReader(is));

        char buff[] = new char[1024];

        bufferedStreamReader.read(buff);


        return new String(buff);
    }

}
