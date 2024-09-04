package com.soprasteria.incidents;

import org.eclipse.jetty.server.CustomRequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.server.config.JettyWebSocketServletContainerInitializer;

import java.util.UUID;

public class IncidentServer {
    private final Server server = new Server(3000);
    private final IncidentProcessor processor = new IncidentProcessor();

    private void start() throws Exception {
        var context = new ServletContextHandler();
        var initializer = new JettyWebSocketServletContainerInitializer(
                (_, container) -> container
                        .addMapping("/ws", (_, _) -> new IncidentWebSocketAdapter(
                                processor
                        ))
        );
        context.addServletContainerInitializer(
                initializer
        );
        server.setHandler(context);
        server.setRequestLog(new CustomRequestLog());
        server.start();

        processor.process(new CreateIncidentEventDto()
                .setIncidentId(UUID.randomUUID())
                .setInfo(new IncidentInfoDto()
                        .setDescription("Test")
                        .setIncidentType(IncidentTypeDto.fire)
                        .setLocation("Oslo Spektrum")
                ));
    }

    public static void main(String[] args) throws Exception {
        new IncidentServer().start();
    }

}
