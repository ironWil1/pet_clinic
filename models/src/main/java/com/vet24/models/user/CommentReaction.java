package com.vet24.models.user;

import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class CommentReaction{


    @EmbeddedId
    @EqualsAndHashCode.Include
    private CommentReactionId id;

    @ManyToOne(fetch= FetchType.LAZY)
    @PrimaryKeyJoinColumn
    @MapsId("commentId")
    private Comment comment;


    @ManyToOne(fetch= FetchType.LAZY)
    @PrimaryKeyJoinColumn
    @MapsId("clientId")
    private Client client;

    @Column(nullable = false)
    private Boolean positive;

    public CommentReaction(Comment comment, Client client, Boolean positive) {
        this.id = new CommentReactionId(comment.getId(),client.getId());
        this.comment = comment;
        this.client = client;
        this.positive = positive;
    }
}
