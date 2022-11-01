package com.vet24.models.notification;

import com.vet24.models.enums.RemindType;

public class EmailRemind extends Remind<String> {

    public EmailRemind(String content, String receiver) {
        super(content, receiver);
        this.type = RemindType.EMAIL;
    }
}
