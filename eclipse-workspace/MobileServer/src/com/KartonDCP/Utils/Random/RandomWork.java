package com.KartonDCP.Utils.Random;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public final class RandomWork {

    private static String formatTheRequest(String methodName, Map<String, String> args, String appToken) {
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


    public static final String getRandWord(int targetStringLength) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    public static final Map<String, String> createRandUserArgs(){
        var args = new HashMap<String, String>();

        args.put("name", getRandWord(6));
        args.put("surname", getRandWord(10));
        args.put("password", getRandWord(5) + (new Random()).nextInt(100));

        var rand = new Random();
        args.put("phone_num", "8" + rand.nextInt(999999999) + rand.nextInt(10));

        return args;
    }


    public static String requestRandUserReg(String token){
        return formatTheRequest("register", createRandUserArgs(), token);
    }



}
