package com.vet24.vaadin.models.user;

import com.vet24.vaadin.models.enums.RoleNameEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(of = {"name"})
public class Role {

    @NonNull
    private RoleNameEnum name;
}
