package com.vet24.models.dto.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.enums.WorkShift;
import com.vet24.models.util.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorScheduleDto {

    @NotNull(groups = OnUpdate.class)
    @Null(groups = OnCreate.class)
    @JsonView({View.Get.class, View.Put.class})
    private Long id;

    @NotNull(groups = OnCreate.class)
    @Null(groups = OnUpdate.class)
    @JsonView({View.Get.class, View.Post.class})
    private Long doctorId;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @JsonView({View.Get.class, View.Post.class, View.Put.class})
    private WorkShift workShift;

    @Min(1)
    @Max(53)
    @NotNull(groups = OnCreate.class)
    @Null(groups = OnUpdate.class)
    @JsonView({View.Get.class, View.Post.class})
    private Integer weekNumber;
}
