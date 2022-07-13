package com.vet24.models.user;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "profile")
public class Profile {
    @Id
    @Column(name = "id")
    private Long id;

    @NonNull
    @OneToOne(fetch = FetchType.LAZY)
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

    public Profile(@NonNull User user, @NonNull String firstName, @NonNull String lastName) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Profile profile = (Profile) o;
        return id != null && Objects.equals(id, profile.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
