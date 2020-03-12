package com.company.Handlers;

import java.net.Socket;

public interface Handler{
    void handleResponse();
    String getResponseType();
}
