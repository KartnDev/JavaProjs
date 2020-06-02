package com.KartonDCP.Server.MobileSever.ProtocolAndInet;

import com.KartonDCP.Utils.Exceptions.InvalidRequestException;
import com.KartonDCP.Utils.Exceptions.TokenException;

import java.util.HashMap;
import java.util.Map;

public class ProtocolParser {
    private final String requestStr;
    private final String token;
    private String[] split;

    public ProtocolParser(String requestStr, String token) throws InvalidRequestException {
        this.requestStr = requestStr
                .trim()
                .replace(" ", "")
                .replace("\n", "")
                .replace("\t", "")
                .toLowerCase();
        this.token = token;

        if (!correctFormat()) {
            throw new InvalidRequestException("Cannot recognize request format");
        }
        if (!split[0].equals(token.toLowerCase())) {
            throw new TokenException("Client have no key token!");
        }
    }

    private boolean correctFormat() {
        if (requestStr.contains("?")) {
            split = requestStr.split("\\?(?!\\?)");
            return split.length == 3;
        } else {
            return false;
        }

    }


    public ProtocolMethod getMethodName() {
        ProtocolMethod title = switch (split[1]) {
            case "register" -> ProtocolMethod.Register;
            case "reg_session" -> ProtocolMethod.ConnSession;
            default -> ProtocolMethod.BadMethod;
        };
        return title;
    }

    public Map<String, String> getArgs() {
        Map<String, String> map = new HashMap<String, String>();
        if (split[2].contains("&")) {
            for (var pair : split[2].split("&")) {
                var keyValueSplit = pair.split("=");
                map.put(keyValueSplit[0], keyValueSplit[1]);
            }

        } else {
            if (split[2].contains("=")) {
                var keyValueSplit = split[2].split("=");
                map.put(keyValueSplit[0], keyValueSplit[1]);
            }
        }
        return map;
    }
}
