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
import java.util.StringJoiner;
import java.util.stream.Collectors;


@Component
@Aspect
public class CheckExistValidator {

    @PersistenceContext
    private EntityManager entityManager;

    @Around("execution(public * *(.., @CheckExist (*), ..))")
    private Object verify(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Annotation[][] annotationMatrix = methodSignature.getMethod().getParameterAnnotations();
        Object[] args = joinPoint.getArgs();
        List<Class<?>> entityClassList = new ArrayList<>();
        List<Long> idArgs = Arrays.stream(args)
                .filter(this::isCanParse)
                .map(x -> Long.parseLong(x.toString()))
                .collect(Collectors.toList());
        for (Annotation[] annotations : annotationMatrix) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof CheckExist) {
                    entityClassList.add(((CheckExist) annotation).entityClass());
                }
            }
        }

        StringJoiner stringJoiner = new StringJoiner(",");
        for (Class<?> clazz : entityClassList) {
            if (entityManager.find(clazz, idArgs.get(entityClassList.indexOf(clazz))) == null) {
                stringJoiner.add(clazz.getSimpleName());
                throw new BadRequestException(String.format("Сущность %s с указанным id %d не существует!",
                        stringJoiner, idArgs.get(entityClassList.indexOf(clazz))));
            }
        }
        return joinPoint.proceed();
    }

    private boolean isCanParse(Object arg) {
        long result = 0;
        try {
            result = Long.parseLong(arg.toString());
        } catch (Exception ignored) {
        }
        return result != 0;
    }
}

