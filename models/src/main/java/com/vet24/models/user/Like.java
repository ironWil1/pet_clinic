package com.vet24.models.user;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class Like {

    @EmbeddedId
    private LikeId likeId;

    @Column(nullable = false)
    private Boolean dislike;
}
