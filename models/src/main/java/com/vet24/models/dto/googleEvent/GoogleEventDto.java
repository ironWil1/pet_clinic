package com.vet24.models.dto.googleEvent;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class GoogleEventDto {
    String summary;
    String location;
    String description;
    Timestamp startDate;
    Timestamp endDate;
    String email;
}
