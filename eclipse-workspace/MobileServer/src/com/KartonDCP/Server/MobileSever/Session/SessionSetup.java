package com.KartonDCP.Server.MobileSever.Session;

import com.KartonDCP.Server.DatabaseWorker.Models.UserEntity;

import java.util.UUID;

public class SessionSetup {

    private final int timeSeconds;
    private final UUID sessionToken;
    private final UserEntity logUser;

    public SessionSetup(int timeSeconds, UUID sessionToken, UserEntity logUser){

        this.timeSeconds = timeSeconds;
        this.sessionToken = sessionToken;
        this.logUser = logUser;
    }

    public SessionSetup(int timeSeconds, UUID sessionToken){

        this.timeSeconds = timeSeconds;
        this.sessionToken = sessionToken;
        this.logUser = null;
    }

    public UUID getSessionToken() {
        return sessionToken;
    }

    public int getTimeSeconds() {
        return timeSeconds;
    }
}
