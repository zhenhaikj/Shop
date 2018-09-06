package com.emjiayuan.app.event;

public class IsReadEvent {
    private int  position;

    public IsReadEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
