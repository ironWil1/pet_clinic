package com.vet24.models.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne (fetch = FetchType.LAZY)
    private User user;

    @Column(nullable=false)
    private String content;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateTime;

    @OneToMany(
            mappedBy = "comment",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CommentReaction> commentReactions = new ArrayList<>();

    public Comment(User user, String content, LocalDateTime dateTime) {
        this.user = user;
        this.content = content;
        this.dateTime = dateTime;
    }

    public Comment(User user, String content) {
        this.user = user;
        this.content = content;
    }
}
