package com.vet24.models.dto.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.enums.WorkShift;
import com.vet24.models.validation.FirstDayOfWeek;
import com.vet24.models.util.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorScheduleDto {

    @Null(groups = {OnCreate.class, OnUpdate.class})
    @JsonView({View.Get.class})
    private Long id;

    @NotNull(groups = OnCreate.class)
    @Null(groups = OnUpdate.class)
    @JsonView({View.Get.class, View.Post.class})
    private Long doctorId;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @JsonView({View.Get.class, View.Post.class, View.Put.class})
    private WorkShift workShift;

    @NotNull(groups = OnCreate.class)
    @Null(groups = OnUpdate.class)
    @FirstDayOfWeek(groups = {OnCreate.class})
    @JsonView({View.Get.class, View.Post.class})
    private LocalDate startWeek;
}
