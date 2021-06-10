package com.vet24.models.user;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@IdClass(CommentReactionId.class)
public class CommentReaction {

    @Id
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    @EqualsAndHashCode.Include
    private Comment comment;

    @Id
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    @EqualsAndHashCode.Include
    private Client client;

    @Column(nullable = false)
    private Boolean positive;
}
