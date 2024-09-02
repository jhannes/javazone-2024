package com.soprasteria.incidents;

public interface IncidentEventDto extends MessageFromServerDto {
    static Class<? extends IncidentEventDto> getType(String event) {
        return switch (event) {
            case "CreateIncidentEvent" -> CreateIncidentEventDto.class;
            case "AddPersonToIncidentEvent" -> AddPersonToIncidentEventDto.class;
            default -> throw new IllegalArgumentException("Invalid IncidentEvent " + event);
        };
    }

    String getEvent();
}
