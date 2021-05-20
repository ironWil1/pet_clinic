package com.vet24.models.user;

import com.vet24.models.enums.RoleNameEnum;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;


@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(of = {"name"})
public class Role implements GrantedAuthority {

    @Id
    @NonNull
    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "varchar(25)")
    private RoleNameEnum name;

    @Override
    public String getAuthority() {
        return name.toString();
    }

}
