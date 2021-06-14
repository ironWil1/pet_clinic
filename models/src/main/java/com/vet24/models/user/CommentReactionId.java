package com.vet24.models.user;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;


@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class CommentReactionId implements Serializable {

    @EqualsAndHashCode.Include
    @Column(nullable= false)
    private Comment comment;

    @EqualsAndHashCode.Include
    @Column(nullable= false)
    private Client client;

}
