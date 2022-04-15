package com.vet24.models.dto.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.vet24.models.dto.OnCreate;
import com.vet24.models.user.Doctor;
import com.vet24.models.util.View;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.ManyToOne;
import javax.validation.constraints.Null;


@Data
@AllArgsConstructor
public class DoctorDtoPost {
    @JsonView(View.Get.class)
    private Long id;
    @JsonView({View.Get.class, View.Post.class})
    private String email;
    private String firstname;
    private String lastname;
    private String password;
    private String avatar;

}
