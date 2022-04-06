package com.vet24.models.user;

//import com.vet24.models.annotation.CreateAuthor;
import com.vet24.models.annotation.CreateAuthor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private User user;

    @CreateAuthor
    @OneToOne(cascade = CascadeType.ALL)
    private User activeUser;

    @Column(nullable = false)
    private String content;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateTime;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinTable(name = "topic_comments",
            joinColumns = @JoinColumn(name = "comments_id"),
            inverseJoinColumns = @JoinColumn(name = "topic_id"))
    private Topic topic;

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
