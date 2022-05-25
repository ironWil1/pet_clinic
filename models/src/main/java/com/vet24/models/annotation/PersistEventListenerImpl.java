package com.vet24.models.annotation;

import com.vet24.models.user.User;
import com.vet24.models.util.ReflectionUtil;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.PersistEvent;
import org.hibernate.event.spi.PersistEventListener;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Field;
import java.util.Map;

import static com.vet24.models.secutity.SecurityUtil.getSecurityUserOrNull;

public class PersistEventListenerImpl implements PersistEventListener {
    public static final PersistEventListenerImpl INSTANCE = new PersistEventListenerImpl();
    private final ReflectionUtil reflectionUtil = new ReflectionUtil();

    @Override
    public void onPersist(PersistEvent persistEvent) throws HibernateException {

        final Object entity = persistEvent.getObject();
        reflectionUtil.searchAnnotationCreateAuthor(entity.getClass(), entity);
    }


    @Override
    public void onPersist(PersistEvent persistEvent, Map map) throws HibernateException {

    }
}



