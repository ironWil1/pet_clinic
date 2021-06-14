package com.vet24.models.user;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VerificationToken  implements Serializable {
    @Id
    @NonNull
    @EqualsAndHashCode.Include
    private Long id;

    @NonNull
    @OneToOne(targetEntity = Client.class,cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private Client client;

}