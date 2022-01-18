package com.vet24.models.user;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User topicStarter;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private boolean isClosed;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "topic_comments",
            joinColumns = @JoinColumn(name = "topic_id"),
            inverseJoinColumns = @JoinColumn(name = "comments_id"))
    private List<Comment> comments;

    public Topic(User topicStarter, String title, String content, boolean isClosed, List<Comment> comments) {
        this.topicStarter = topicStarter;
        this.title = title;
        this.content = content;
        this.isClosed = isClosed;
        this.comments = comments;
    }

    public Topic(LocalDateTime creationDate, LocalDateTime lastUpdateDate) {
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdateDate;
    }
}
