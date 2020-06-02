package com.KartonDCP.SDK;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class ReqFormatter {

    public static String formatTheRequest(String methodName, Map<String, String> args, String appToken) {
        StringBuffer sb = new StringBuffer();

        args.forEach((key, value) -> { // я че в джаваскрипт попал???
            sb.append(key);
            sb.append("=");
            sb.append(value);
            sb.append("&");
        });

        sb.deleteCharAt(sb.length() - 1);

        return String.format("%s?%s?%s", appToken, methodName, sb.toString());
    }


    public static String formatRegister(String name, String surname, String password, String phone_num, String token) {
        var args = new HashMap<String, String>();
        args.put("name", name);
        args.put("surname", surname);
        args.put("password", password);
        args.put("phone_num", phone_num);

        return formatTheRequest("register", args, token);
    }
}
