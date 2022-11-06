package com.vet24.models.notification;

public interface RemindConverter<T>{

    Remind<T> convert(UserNotification notification);
}
