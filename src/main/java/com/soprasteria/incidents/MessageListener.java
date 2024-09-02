package com.soprasteria.incidents;

public interface MessageListener {
    void onMessage(MessageFromServerDto message);
}
