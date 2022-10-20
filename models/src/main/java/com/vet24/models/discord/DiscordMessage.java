package com.vet24.models.discord;

import com.vet24.models.news.News;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "discord_message")
public class DiscordMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "discord_msg_id")
    @NotNull
    private Long discordMsgId;

    @Column(name = "channel_id")
    private Long channelId;

    @OneToOne(mappedBy = "discordMessage",
            cascade = {CascadeType.DETACH,CascadeType.REFRESH,
                    CascadeType.PERSIST,CascadeType.MERGE})
    private News news;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DiscordMessage dm = (DiscordMessage) o;
        return Objects.nonNull(id) && Objects.equals(id, dm.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}