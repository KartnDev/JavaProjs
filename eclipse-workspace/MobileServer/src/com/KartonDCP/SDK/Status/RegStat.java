package com.KartonDCP.SDK.Status;

import com.sun.istack.Nullable;

import java.util.UUID;

public class RegStat {
    private final RegStatusCode code;
    private final UUID userToken;

    public RegStat(RegStatusCode code,@Nullable UUID userToken){

        this.code = code;
        this.userToken = userToken;
    }

    public RegStatusCode getCode() {
        return code;
    }

    public UUID getUserToken() {
        return userToken;
    }
}
