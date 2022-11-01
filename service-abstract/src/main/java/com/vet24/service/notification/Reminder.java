package com.vet24.service.notification;

import java.util.List;

public interface Reminder<T> {
    void send(T remind);
    void send(List<T> remind);
}
