package com.vet24.models.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"commentReactions"})
@Entity

public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    private Client client;

    @Column
    private String content;

    @Column
    private LocalDateTime dateTime;

    @ManyToOne (fetch = FetchType.LAZY)
    private Doctor doctor;

    @OneToMany(
            mappedBy = "comment",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CommentReaction> commentReactions;

    public Comment(Client client, String content, LocalDateTime dateTime, Doctor doctor) {
        this.client = client;
        this.content = content;
        this.dateTime = dateTime;
        this.doctor = doctor;
    }
}
