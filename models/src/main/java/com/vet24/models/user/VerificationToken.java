package com.vet24.models.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class VerificationToken {
    @Id
    @NonNull
    private Long id;

    @NonNull
    @OneToOne(targetEntity = Client.class,cascade =
            {CascadeType.PERSIST,CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private Client client;

}