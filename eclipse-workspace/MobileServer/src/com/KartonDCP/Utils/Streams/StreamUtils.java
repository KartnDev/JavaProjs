package com.KartonDCP.Utils.Streams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class StreamUtils {
    public final static String InputStreamToString(InputStream is) throws IOException {
        BufferedReader bufferedStreamReader = new BufferedReader(new InputStreamReader(is));

        char buff[] = new char[1024];
        bufferedStreamReader.read(buff);

        return new String(buff);
    }
}
