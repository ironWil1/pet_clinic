package com.vet24.models.user;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class LikeId implements Serializable {

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Comment comment;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Client client;

}
