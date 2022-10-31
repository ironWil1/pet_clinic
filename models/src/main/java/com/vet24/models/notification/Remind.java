package com.vet24.models.notification;

import com.vet24.models.enums.RemindType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Remind<T> {

    T content;
    String receiver;

    @Setter(AccessLevel.NONE)
    RemindType type;

    Remind(T content, String receiver) {
        this.content = content;
        this.receiver = receiver;
    }
}
