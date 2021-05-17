package com.vet24.models.dto.pet.procedure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.vet24.models.enums.ProcedureType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = VaccinationDto.class, name = "VACCINATION"),
        @JsonSubTypes.Type(value = ExternalParasiteDto.class, name = "EXTERNAL_PARASITE"),
        @JsonSubTypes.Type(value = EchinococcusDto.class, name = "ECHINOCOCCUS")
})
public abstract class AbstractNewProcedureDto {
    Long id;
    LocalDate date; //if null or blank set now
    ProcedureType type;
    Long medicineId;
    String medicineBatchNumber;
    Boolean isPeriodical;
    Integer periodDays;

    @JsonCreator
    protected AbstractNewProcedureDto(Long id, LocalDate date, ProcedureType type, Long medicineId,
                                      String medicineBatchNumber, Boolean isPeriodical, Integer periodDays) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.medicineId = medicineId;
        this.medicineBatchNumber = medicineBatchNumber;
        this.isPeriodical = isPeriodical;
        this.periodDays = periodDays;
    }
}

