package com.vet24.models.track;


import com.vet24.models.annotation.CreateAuthor;
import com.vet24.models.annotation.UpdateAuthor;
import com.vet24.models.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class ChangeTrackedEntity {
    @CreationTimestamp
    private LocalDateTime creationDateTime;
    @UpdateTimestamp
    private LocalDateTime lastUpdateDateTime;
    @ManyToOne
    @CreateAuthor
    private User createAuthor;
    @ManyToOne
    @UpdateAuthor
    private User lastUpdateAuthor;
}


