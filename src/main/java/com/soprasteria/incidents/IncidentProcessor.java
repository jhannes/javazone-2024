package com.soprasteria.incidents;

import java.util.ArrayList;
import java.util.List;

public class IncidentProcessor {
    private final List<IncidentSnapshotDto> snapshots = new ArrayList<>();

    public void process(CreateIncidentEventDto createEvent) {
        snapshots.add(new IncidentSnapshotDto()
                .setIncidentId(createEvent.getIncidentId())
                .setIncidentType(createEvent.getIncidentType())
                .setDescription(createEvent.getDescription())
        );
    }

    public List<IncidentSnapshotDto> list() {
        return snapshots;
    }
}
