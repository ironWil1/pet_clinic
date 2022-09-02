package com.vet24.models.annotation;


import com.vet24.models.util.ReflectionUtil;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.MergeEvent;
import org.hibernate.event.spi.MergeEventListener;

import java.lang.reflect.Field;
import java.util.Map;

import static com.vet24.models.secutity.SecurityUtil.getOptionalOfNullableSecurityUser;

public class MergeEventListenerImpl implements MergeEventListener {
    public static final MergeEventListenerImpl INSTANCE = new MergeEventListenerImpl();


    @Override
    public void onMerge(MergeEvent mergeEvent) throws HibernateException {
        getOptionalOfNullableSecurityUser().ifPresent(activeUser -> {
            final Object entity = mergeEvent.getEntity();
            Field field = ReflectionUtil.searchFieldWithAnnotation(entity.getClass(), UpdateAuthor.class);
            if (field != null) {
                try {
                    field.setAccessible(true);
                    field.set(entity, activeUser);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onMerge(MergeEvent mergeEvent, Map map) throws HibernateException {

    }
}





