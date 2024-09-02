package com.soprasteria.incidents;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class IncidentSnapshotListDto implements MessageFromServerDto {
    private List<IncidentSnapshotDto> list = new ArrayList<>();
}
