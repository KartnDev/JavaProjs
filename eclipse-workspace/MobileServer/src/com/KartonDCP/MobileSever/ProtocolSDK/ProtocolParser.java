package com.KartonDCP.MobileSever.ProtocolSDK;

import java.util.Map;

public class ProtocolParser {
    private final String requestStr;
    private final String token;
    private String[] split;

    public ProtocolParser(String requestStr, String token){
        this.requestStr = requestStr.trim().replace(" ", "")
                .replace("\n", "")
                .replace("\t", "");
        this.token = token;

        if(!correctFormat()){
            throw new
        }

    }

    public boolean correctFormat(){
        if(requestStr.contains("?")){
            split = requestStr.split("/?");
            return split.length == 3 && split[0].equals(token);
        }
        else {
            return false;
        }

    }


    public ProtocolMethod getMethodName(){
        if(requestStr.contains("?")){

        }
    }
    public Map<String, String> getArgs(){

    }
}
