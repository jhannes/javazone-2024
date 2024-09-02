package com.soprasteria.incidents;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateIncidentEventDto implements IncidentEventDto {
    private String event = "CreateIncidentEvent";
    private UUID incidentId;
    private IncidentTypeDto incidentType;
    private String description;
}
