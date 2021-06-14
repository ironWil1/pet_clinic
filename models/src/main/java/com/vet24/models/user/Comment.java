package com.vet24.models.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    private Client client;

    @Column(nullable=false)
    private String content;

    @Column(nullable=false)
    private LocalDateTime dateTime;

    @ManyToOne (fetch = FetchType.LAZY)
    private Doctor doctor;

    @OneToMany(
            mappedBy = "comment",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CommentReaction> commentReactions = new ArrayList<>();

    public Comment(Client client, String content, LocalDateTime dateTime, Doctor doctor) {
        this.client = client;
        this.content = content;
        this.dateTime = dateTime;
        this.doctor = doctor;
    }
}
