package com.soprasteria.incidents;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class IncidentEventProcessorTest {
    @Test
    void shouldCreateIncident() {
        var incidentProcessor = new IncidentProcessor();
        var createEvent = new CreateIncidentEventDto()
                .setIncidentId(UUID.randomUUID())
                .setIncidentType(IncidentTypeDto.fire)
                .setDescription("Fire in the disco!");
        incidentProcessor.process(createEvent);
        assertThat(incidentProcessor.list()).isEqualTo(List.of(
                new IncidentSnapshotDto()
                        .setIncidentId(createEvent.getIncidentId())
                        .setIncidentType(createEvent.getIncidentType())
                        .setDescription(createEvent.getDescription())
        ));
    }
}
