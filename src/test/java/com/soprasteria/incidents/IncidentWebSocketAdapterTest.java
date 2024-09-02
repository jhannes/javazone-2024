package com.soprasteria.incidents;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.eclipse.jetty.websocket.server.config.JettyWebSocketServletContainerInitializer;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class IncidentWebSocketAdapterTest {

    @Test
    void shouldTransmitSnapshotsToClient() throws Exception {
        var processor = new IncidentProcessor();
        var createIncident = new CreateIncidentEventDto()
                .setIncidentId(UUID.randomUUID())
                .setDescription("Test incident");
        processor.process(createIncident);

        var server = new Server(0);
        var context = new ServletContextHandler();
        context.addServletContainerInitializer(new JettyWebSocketServletContainerInitializer(
                (_, container) -> container.addMapping("/ws", (req, resp) -> new IncidentWebSocketAdapter())
        ));
        server.setHandler(context);
        server.start();

        var messages = new ArrayBlockingQueue<String>(10);

        var webSocketClient = new WebSocketClient();
        webSocketClient.start();
        webSocketClient.connect(new WebSocketAdapter() {
            @Override
            public void onWebSocketText(String message) {
                messages.add(message);
            }
        }, URI.create("ws://localhost:" + server.getURI().getPort() + "/ws"));

        assertThat(messages.poll(1, TimeUnit.SECONDS)).isNotNull();

    }

}
