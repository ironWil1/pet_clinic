package com.vet24.models.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "profiles")
public class Profile {
    @Id
    @Column(name = "id")
    private Long id;

    @NonNull
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Nullable
    @Column(name = "avatar_url")
    private String avatarUrl;

    @Nullable
    @Column(name = "first_name")
    private String firstName;

    @Nullable
    @Column(name = "last_name")
    private String lastName;

    @Nullable
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Nullable
    @Column(name = "discord_id")
    private String discordId;

    @Nullable
    @Column(name = "telegram_id")
    private String telegramId;

    @NonNull
    @Column(name = "discord_notify")
    private Boolean discordNotify;

    @NonNull
    @Column(name = "email_notify")
    private Boolean emailNotify;

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
