package com.vet24.models.user;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@Entity@IdClass(CommentReactionId.class)
public class CommentReaction{


    @Id
    @ManyToOne(fetch= FetchType.LAZY)
    @PrimaryKeyJoinColumn
    @EqualsAndHashCode.Include
    private Comment comment;

    @Id
    @ManyToOne(fetch= FetchType.LAZY)
    @PrimaryKeyJoinColumn
    @EqualsAndHashCode.Include
    private Client client;

    @Column(nullable = false)
    private Boolean positive;
}
