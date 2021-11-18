package com.vet24.models.dto.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnCreate;
import com.vet24.models.dto.OnUpdate;
import com.vet24.models.enums.WorkShift;
import com.vet24.models.util.Get;
import com.vet24.models.util.Post;
import com.vet24.models.util.Put;
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
    @JsonView({Get.class, Put.class})
    private Long id;

    @NotNull(groups = OnCreate.class)
    @Null(groups = OnUpdate.class)
    @JsonView({Get.class, Post.class})
    private Long doctorId;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @JsonView({Get.class, Post.class, Put.class})
    private WorkShift workShift;

    @Min(1)
    @Max(53)
    @NotNull(groups = OnCreate.class)
    @Null(groups = OnUpdate.class)
    @JsonView({Get.class, Post.class})
    private Integer weekNumber;
}
