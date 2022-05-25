package com.vet24.models.annotation;


import com.vet24.models.util.ReflectionUtil;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.MergeEvent;
import org.hibernate.event.spi.MergeEventListener;
import java.util.Map;

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





