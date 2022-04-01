package com.vet24.models.annotation;


import com.vet24.models.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.persistence.ManyToOne;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class CreateAuthorHandler {

    @ManyToOne
    User setUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    public User getActiveUser() {
        return setUser;
    }

    @CreateAuthor
    public void methodSet(Object obj, Class obj1) throws IllegalAccessException {
        Field[] fields = obj1.getDeclaredFields();
        for (Field field : fields){
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations){
                if (annotation.annotationType().equals(CreateAuthor.class)){
                    field.set(obj, setUser);

                }
            }
        }
    }
}



