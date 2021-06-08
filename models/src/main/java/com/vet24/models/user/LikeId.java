package com.vet24.models.user;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class LikeId implements Serializable {


    private Comment comment;

    private Client client;

}
