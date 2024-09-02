package com.soprasteria.incidents;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.eclipse.jetty.websocket.server.config.JettyWebSocketServletContainerInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class IncidentWebSocketAdapterTest {

    private IncidentProcessor processor;
    private WebSocketClient webSocketClient = new WebSocketClient();

    public IncidentWebSocketAdapterTest() {
        processor = new IncidentProcessor();
    }

    @BeforeEach
    void setUp() throws Exception {
        webSocketClient.start();
    }

    @Test
    void shouldTransmitSnapshotsToClient() throws Exception {
        var createIncident = new CreateIncidentEventDto()
                .setIncidentId(UUID.randomUUID())
                .setDescription("Test incident");
        processor.process(createIncident);
        var server = createServer();

        var messages = new ArrayBlockingQueue<String>(10);
        webSocketClient.connect(new WebSocketAdapter() {
            @Override
            public void onWebSocketText(String message) {
                messages.add(message);
            }
        }, URI.create("ws://localhost:" + server.getURI().getPort() + "/ws"));

        assertThat(IncidentWebSocketAdapter.objectMapper.readValue(
                messages.poll(1, TimeUnit.SECONDS),
                IncidentSnapshotListDto.class
        ).getList()).isEqualTo(List.of(new IncidentSnapshotDto()
                .setIncidentId(createIncident.getIncidentId())
                .setDescription(createIncident.getDescription())
        ));
    }

    private Server createServer() throws Exception {
        var server = new Server(0);
        var context = new ServletContextHandler();
        context.addServletContainerInitializer(new JettyWebSocketServletContainerInitializer(
                (_, container) -> container.addMapping("/ws", (req, resp) -> new IncidentWebSocketAdapter(processor))
        ));
        server.setHandler(context);
        server.start();
        return server;
    }

}
