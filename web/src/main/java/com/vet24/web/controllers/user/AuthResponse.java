package com.vet24.web.controllers.user;

import com.vet24.models.enums.RoleNameEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    String jwtToken;
    RoleNameEnum role;



}
