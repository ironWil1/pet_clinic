package com.vet24.models.notification;

public interface RemindConverter{

    Remind<String> convert(UserNotification notification);
}
