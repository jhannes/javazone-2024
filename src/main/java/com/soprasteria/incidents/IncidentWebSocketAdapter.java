package com.soprasteria.incidents;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class IncidentWebSocketAdapter extends WebSocketAdapter {

    public static ObjectMapper objectMapper = new ObjectMapper();
    private final IncidentProcessor processor;

    public IncidentWebSocketAdapter(IncidentProcessor processor) {
        this.processor = processor;
    }

    @SneakyThrows
    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        getRemote().sendString(objectMapper.writeValueAsString(new IncidentSnapshotListDto()
                .setList(processor.list())
        ));
    }
}
