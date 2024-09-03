package com.soprasteria.incidents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class IncidentProcessor {
    private final Map<UUID, IncidentSnapshotDto> snapshots = new HashMap<>();
    private final Set<MessageListener> listeners = new HashSet<>();

    public void process(IncidentEventDto event) {
        if (event instanceof CreateIncidentEventDto createEvent) {
            snapshots.put(createEvent.getIncidentId(), new IncidentSnapshotDto()
                    .setIncidentId(createEvent.getIncidentId())
                    .setIncidentType(createEvent.getIncidentType())
                    .setDescription(createEvent.getDescription())
                    .setLocation(createEvent.getLocation())
            );
        } else if (event instanceof AddPersonToIncidentEventDto addPerson) {
            snapshots.get(addPerson.getIncidentId())
                    .getPersons()
                    .put(addPerson.getPersonId(), new PersonSnapshotDto()
                            .setPersonName(addPerson.getPersonName())
                            .setRole(addPerson.getRole())
                    );
        }
        for (var listener : listeners) {
            listener.onMessage(event);
        }
    }

    public List<IncidentSnapshotDto> list() {
        return new ArrayList<>(snapshots.values());
    }

    public void subscribe(MessageListener listener) {
        this.listeners.add(listener);
    }

    public void unsubscribe(MessageListener listener) {
        this.listeners.remove(listener);
    }
}
