package com.vet24.models.annotation;

import com.vet24.models.user.User;
import org.hibernate.event.spi.*;
import org.springframework.security.core.context.SecurityContextHolder;
import java.lang.reflect.Field;


public class ReplicationInsertEventListener implements PreInsertEventListener {

    public static final ReplicationInsertEventListener INSTANCE =
            new ReplicationInsertEventListener();


    @Override
    public boolean onPreInsert(PreInsertEvent preInsertEvent) {
        final Object entity = preInsertEvent.getEntity();

        for (Field fields : entity.getClass().getDeclaredFields()) {
            if (fields.isAnnotationPresent(CreateAuthor.class)) {
                try {
                    User activeUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    fields.setAccessible(true);
                    fields.set(entity, activeUser);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}