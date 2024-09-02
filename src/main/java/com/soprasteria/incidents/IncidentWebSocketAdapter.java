package com.soprasteria.incidents;

import lombok.SneakyThrows;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class IncidentWebSocketAdapter extends WebSocketAdapter {

    @SneakyThrows
    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        getRemote().sendString("Hello world");
    }
}
