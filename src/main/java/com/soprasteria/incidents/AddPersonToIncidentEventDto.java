package com.soprasteria.incidents;

import lombok.Data;

import java.util.UUID;

@Data
public class AddPersonToIncidentEventDto implements IncidentEventDto {
    private UUID incidentId;
    private UUID personId;
    private String personName;
    private PersonRoleDto role;
}
