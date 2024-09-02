package com.soprasteria.incidents;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class IncidentSnapshotDto {
    private UUID incidentId;
    private IncidentTypeDto incidentType;
    private String description;

    private Map<UUID, PersonSnapshotDto> persons = new HashMap<>();
}
