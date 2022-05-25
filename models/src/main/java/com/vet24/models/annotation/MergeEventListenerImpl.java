package com.vet24.models.annotation;

import com.vet24.models.user.User;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.MergeEvent;
import org.hibernate.event.spi.MergeEventListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import static com.vet24.models.secutity.SecurityUtil.getSecurityUserOrNull;

public class MergeEventListenerImpl implements MergeEventListener {
    public static final MergeEventListenerImpl INSTANCE = new MergeEventListenerImpl();

    @Override
    public void onMerge(MergeEvent mergeEvent) throws HibernateException {
        final Object entity = mergeEvent.getEntity();
        Class superClass = entity.getClass().getSuperclass();
        if (superClass.getSimpleName().equals("ChangeTrackedEntity")) {
            for (Field fields : superClass.getDeclaredFields()) {
                if (fields.isAnnotationPresent(UpdateAuthor.class)) {
                    try {
                        User activeUser = getSecurityUserOrNull();

                        fields.setAccessible(true);
                        fields.set(entity, activeUser);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            for (Field fields : entity.getClass().getDeclaredFields()) {
                if (fields.isAnnotationPresent(UpdateAuthor.class)) {
                    try {
                        User activeUser = getSecurityUserOrNull();

                        fields.setAccessible(true);
                        fields.set(entity, activeUser);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onMerge(MergeEvent mergeEvent, Map map) throws HibernateException {

    }
}





