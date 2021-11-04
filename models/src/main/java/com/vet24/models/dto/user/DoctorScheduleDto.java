package com.vet24.models.dto.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnCreateDoctorSchedule;
import com.vet24.models.dto.OnUpdateDoctorSchedule;
import com.vet24.models.enums.WorkShift;
import com.vet24.models.util.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorScheduleDto {

    @JsonView(View.PutAdminSchedule.class)
    @NotNull(groups = {OnUpdateDoctorSchedule.class})
    private Long id;

    @JsonView({View.PostAdminSchedule.class})
    private UserInfoDto doctorInfo;

    @JsonView({View.PostAdminSchedule.class, View.PutAdminSchedule.class})
    @NotNull(groups = {OnCreateDoctorSchedule.class, OnUpdateDoctorSchedule.class})
    private WorkShift workShift;

    @JsonView({View.PostAdminSchedule.class, View.PutAdminSchedule.class})
    @Min(1)
    @Max(53)
    private Integer weekNumber;
}
