package com.vet24.models.news;

import com.vet24.models.discord.DiscordMessage;
import com.vet24.models.enums.NewsType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.FetchType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "news")
public class News implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private long id;

    @Column(name = "type", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private NewsType type;

    @Column(name = "content")
    private String content;

    @Column(name = "title")
    private String title;

    @Column(name = "is_important")
    private boolean isImportant;

    @Column(name = "published", columnDefinition = "boolean default false")
    private boolean published;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @ElementCollection
    @Column(name = "pictures")
    List<String> pictures;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private DiscordMessage discordMessage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        News news = (News) o;
        return Objects.equals(id, news.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
