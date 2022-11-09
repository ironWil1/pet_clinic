package com.vet24.models.dto.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.enums.RoleNameEnum;
import com.vet24.models.util.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonView({View.Put.class, View.Post.class})
public class UserRequestDto extends ProfileDto {
    @NotBlank(message = "Поле email не должно быть пустым")
    private String email;
    @NotNull(message = "Поле role не должно быть null")
    private RoleNameEnum role;
    @NotBlank(message = "Поле password не должно быть пустым")
    private String password;
}
