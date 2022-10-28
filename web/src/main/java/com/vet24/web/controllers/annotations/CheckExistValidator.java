package com.vet24.web.controllers.annotations;


import com.vet24.models.exception.BadRequestException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Component
@Aspect
public class CheckExistValidator {

    @PersistenceContext
    private EntityManager entityManager;

    @Around("execution(public * *(.., @CheckExist (*), ..))")
    private Object verify(ProceedingJoinPoint joinPoint) throws Throwable {
        verifyEntity(getEntityList(joinPoint), getIdList(joinPoint));
        return joinPoint.proceed();
    }

    private void verifyEntity
            (List<Class<?>> entityClassList,List<Long> idArgs) {
        for (Class<?> clazz : entityClassList) {
            Long id = idArgs.get(entityClassList.indexOf(clazz));
            if (entityManager.find(clazz, id) == null) {
                throw new BadRequestException (String.format("Сущность %s с указанным id %d не существует!",
                        clazz.getSimpleName(), id));
            }
        }
    }

    private boolean isCanParse(Object arg) {
        long result = 0;
        try {
            result = Long.parseLong(arg.toString());
        } catch (Exception ignored) {
        }
        return result != 0;
    }

    private List<Long> getIdList(ProceedingJoinPoint joinPoint) {
        return Arrays.stream(joinPoint.getArgs())
                .filter(this::isCanParse)
                .map(x -> Long.parseLong(x.toString()))
                .collect(Collectors.toList());
    }

    private List<Class<?>> getEntityList(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Annotation[][] annotationMatrix = methodSignature.getMethod().getParameterAnnotations();
        List<Class<?>> entityClassList = new ArrayList<>();
        for (Annotation[] annotations : annotationMatrix) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof CheckExist) {
                    entityClassList.add(((CheckExist) annotation).entityClass());
                }
            }
        }
        return entityClassList;
    }
}
