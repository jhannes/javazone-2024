package com.soprasteria.incidents;

import com.fasterxml.jackson.databind.DeserializationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class IncidentProcessor {
    private final Map<UUID, IncidentSnapshotDto> snapshots = new HashMap<>();

    public void process(IncidentEventDto event) {
        if (event instanceof CreateIncidentEventDto createEvent) {
            snapshots.put(createEvent.getIncidentId(), new IncidentSnapshotDto()
                    .setIncidentId(createEvent.getIncidentId())
                    .setIncidentType(createEvent.getIncidentType())
                    .setDescription(createEvent.getDescription())
            );
        } else if (event instanceof AddPersonToIncidentEventDto addPerson) {
            snapshots.get(addPerson.getIncidentId())
                    .getPersons()
                    .put(addPerson.getPersonId(), new PersonSnapshotDto()
                            .setPersonName(addPerson.getPersonName())
                            .setRole(addPerson.getRole())
                    );
        }
    }

    public List<IncidentSnapshotDto> list() {
        return new ArrayList<>(snapshots.values());
    }
}
