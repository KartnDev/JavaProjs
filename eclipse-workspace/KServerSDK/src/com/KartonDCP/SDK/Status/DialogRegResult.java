package com.KartonDCP.SDK.Status;

import java.util.UUID;

public class DialogRegResult{

    private final RegStatusCode statusCode;
    private final UUID dialogToken;

    public DialogRegResult(RegStatusCode statusCode, UUID dialogToken) {
        this.statusCode = statusCode;
        this.dialogToken = dialogToken;
    }


    public UUID getDialogToken() {
        return dialogToken;
    }

    public RegStatusCode getStatusCode() {
        return statusCode;
    }
}
