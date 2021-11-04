package com.vet24.models.dto.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnCreateDoctorSchedule;
import com.vet24.models.util.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {

    @JsonView({View.PostAdminSchedule.class, View.GetAdminSchedule.class})
    @NotNull(groups = OnCreateDoctorSchedule.class)
    private Long id;

    @JsonView(View.GetAdminSchedule.class)
    private String email;

    @JsonView(View.GetAdminSchedule.class)
    private String firstname;

    @JsonView(View.GetAdminSchedule.class)
    private String lastname;
}
