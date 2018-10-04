package org.stuartaroth.httpjava.models;

public class SuccessMessage {
    private boolean success;
    private String message;

    public SuccessMessage(String message) {
        this.success = true;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return message;
    }
}
