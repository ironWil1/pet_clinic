package com.vet24.models.annotation;


import com.vet24.models.user.User;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Field;


public class PreUpdateEventListenerImpl implements PreUpdateEventListener {

    public static final PreUpdateEventListenerImpl INSTANCE =
            new PreUpdateEventListenerImpl();


    @Override
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
        return false;
    }

}
