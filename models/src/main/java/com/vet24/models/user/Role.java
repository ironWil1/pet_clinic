package com.vet24.models.user;

import com.vet24.models.enums.RoleNameEnum;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(of = {"name"})
public class Role implements GrantedAuthority {

    @Id
    @NonNull
    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "varchar(25)")
    @EqualsAndHashCode.Include
    private RoleNameEnum name;


    @Override
    public String getAuthority() {
        return name.toString();
    }

}
