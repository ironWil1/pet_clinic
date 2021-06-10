package com.vet24.models.user;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CommentReactionId implements Serializable {
    private Comment comment;

    private Client client;

}
