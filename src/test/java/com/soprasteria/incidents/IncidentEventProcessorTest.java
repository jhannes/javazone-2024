package com.soprasteria.incidents;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class IncidentEventProcessorTest {

    private final IncidentProcessor incidentProcessor = new IncidentProcessor();

    @Test
    void shouldCreateIncident() {
        var createEvent = createIncidentEvent();
        incidentProcessor.process(createEvent);
        assertThat(incidentProcessor.list()).isEqualTo(List.of(
                new IncidentSnapshotDto()
                        .setIncidentId(createEvent.getIncidentId())
                        .setIncidentType(createEvent.getIncidentType())
                        .setDescription(createEvent.getDescription())
                        .setLocation(createEvent.getLocation())
        ));
    }

    @Test
    void shouldAddPersonToIncident() {
        var createEvent = createIncidentEvent();
        incidentProcessor.process(createEvent);
        var addPersonEvent = new AddPersonToIncidentEventDto()
                .setIncidentId(createEvent.getIncidentId())
                .setPersonId(UUID.randomUUID())
                .setPersonName("Johannes")
                .setRole(PersonRoleDto.witness);
        incidentProcessor.process(addPersonEvent);
        assertThat(incidentProcessor.list().getFirst().getPersons()).isEqualTo(Map.of(
                addPersonEvent.getPersonId(), new PersonSnapshotDto()
                        .setPersonName(addPersonEvent.getPersonName())
                        .setRole(addPersonEvent.getRole())
        ));
    }

    private static CreateIncidentEventDto createIncidentEvent() {
        return new CreateIncidentEventDto()
                .setIncidentId(UUID.randomUUID())
                .setIncidentType(IncidentTypeDto.fire)
                .setDescription("Fire in the disco!")
                .setLocation("Oslo Spektrum");
    }
}
