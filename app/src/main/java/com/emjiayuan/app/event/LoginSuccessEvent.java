package com.emjiayuan.app.event;

public class LoginSuccessEvent {
    private String message;

    public LoginSuccessEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
