package com.vet24.models.annotation;

import com.vet24.models.user.User;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Field;

import static com.vet24.models.secutity.SecurityUtil.getPrincipalOrNull;

public class InsertEventListener implements PreInsertEventListener {

    public static final InsertEventListener INSTANCE =
            new InsertEventListener();


    @Override
    public boolean onPreInsert(PreInsertEvent preInsertEvent) {
        final Object entity = preInsertEvent.getEntity();

        for (Field fields : entity.getClass().getDeclaredFields()) {
            if (fields.isAnnotationPresent(CreateAuthor.class)) {

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    continue;
                }
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