package com.soprasteria.incidents;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import java.io.IOException;

@Slf4j
public class IncidentWebSocketAdapter extends WebSocketAdapter {

    public static ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new SimpleModule()
                    .addDeserializer(IncidentEventDto.class, new JsonDeserializer<>() {
                        @Override
                        public IncidentEventDto deserialize(JsonParser p, DeserializationContext context) throws IOException {
                            var o = (ObjectNode) p.readValueAsTree();
                            return objectMapper.readerFor(IncidentEventDto.getType(o.get("event").asText()))
                                    .readValue(o);
                        }
                    })
            )
            ;
    private final IncidentProcessor processor;
    private final MessageListener listener = this::onMessageFromServer;

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
        processor.subscribe(listener);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        processor.unsubscribe(listener);
    }

    @SneakyThrows
    private void onMessageFromServer(MessageFromServerDto message) {
        getRemote().sendString(objectMapper.writeValueAsString(message));
    }

    @SneakyThrows
    @Override
    public void onWebSocketText(String message) {
        var event = objectMapper.readValue(message, IncidentEventDto.class);
        processor.process(event);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        log.error("onWebSocketError", cause);
    }
}
