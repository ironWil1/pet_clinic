package com.vet24.models.annotation;

import com.vet24.models.user.User;
import com.vet24.models.util.ReflectionUtil;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.MergeEvent;
import org.hibernate.event.spi.MergeEventListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import static com.vet24.models.secutity.SecurityUtil.getSecurityUserOrNull;

public class MergeEventListenerImpl implements MergeEventListener {
    public static final MergeEventListenerImpl INSTANCE = new MergeEventListenerImpl();
    private final ReflectionUtil reflectionUtil = new ReflectionUtil();

    @Override
    public void onMerge(MergeEvent mergeEvent) throws HibernateException {
        final Object entity = mergeEvent.getEntity();
        reflectionUtil.searchAnnotationUpdateAuthor(entity.getClass(), entity);
    }

    @Override
    public void onMerge(MergeEvent mergeEvent, Map map) throws HibernateException {

    }
}





