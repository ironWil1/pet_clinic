package com.vet24.models.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NonNull
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @NonNull
    @Column(name = "avatar_url")
    private String avatarUrl;

    @NonNull
    @Column(name="first_name")
    private String firstName;

    @NonNull
    @Column(name="last_name")
    private String lastName;

    @NonNull
    @Column(name="birth_date")
    private LocalDate birthDate;

    @NonNull
    @Column(name="discord_id")
    private String discordId;

    @NonNull
    @Column(name = "telegram_id")
    private String telegramId;



}
