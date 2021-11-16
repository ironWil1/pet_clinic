package com.vet24.models.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
    @EqualsAndHashCode.Include
    private Long id;

    @MapsId
    @NonNull
    @OneToOne(targetEntity = Client.class,cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, unique = true, name = "user_id")
    private Client client;
}