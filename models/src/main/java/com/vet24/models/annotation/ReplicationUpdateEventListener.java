package com.vet24.models.annotation;


import com.vet24.models.user.User;


import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;


public class ReplicationUpdateEventListener implements PreUpdateEventListener {

    public static final ReplicationUpdateEventListener INSTANCE =
            new ReplicationUpdateEventListener();


    @Override
    @Transactional
    public boolean onPreUpdate(PreUpdateEvent preUpdateEvent) {

        final Object entity = preUpdateEvent.getEntity();

        for (Field fields : entity.getClass().getDeclaredFields()) {
            if (fields.isAnnotationPresent(UpdateAuthor.class)) {
                try {
                    User activeUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    fields.setAccessible(true);
                    fields.set(entity, activeUser);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

}
