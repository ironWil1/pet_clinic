package com.vet24.models.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
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
    private User user;

    @Column(nullable=false)
    private String content;

    @Column(nullable=false)
    private LocalDateTime dateTime;

    @ManyToOne(fetch= FetchType.LAZY)

    private Comment comment;

    public Comment(User user, String content, LocalDateTime dateTime) {
        this.user = user;
        this.content = content;
        this.dateTime = dateTime;
    }
}
