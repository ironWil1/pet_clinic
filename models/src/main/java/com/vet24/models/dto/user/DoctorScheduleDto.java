package com.vet24.models.dto.user;

import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.enums.WorkShift;
import com.vet24.models.user.Doctor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorScheduleDto {

    @Null(groups = {OnCreate.class})
    @NotNull(groups = {OnUpdate.class})
    private Long id;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private Doctor doctor;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private WorkShift workShift;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private Integer weekNumber;
}
