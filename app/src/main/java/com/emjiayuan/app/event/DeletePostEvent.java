package com.emjiayuan.app.event;

public class DeletePostEvent {
    private int  position;

    public DeletePostEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
