package com.vet24.models.annotation;


import com.vet24.models.user.User;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;

import java.lang.reflect.Field;

import static com.vet24.models.secutity.SecurityUtil.getPrincipalOrNull;


public class PreUpdateEventListenerImpl implements PreUpdateEventListener {

    public static final PreUpdateEventListenerImpl INSTANCE =
            new PreUpdateEventListenerImpl();


    @Override
    public boolean onPreUpdate(PreUpdateEvent preUpdateEvent) {

        final Object entity = preUpdateEvent.getEntity();

        for (Field fields : entity.getClass().getDeclaredFields()) {
            if (fields.isAnnotationPresent(UpdateAuthor.class)) {
                try {
//                    User activeUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    User activeUser = (User) getPrincipalOrNull();
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
