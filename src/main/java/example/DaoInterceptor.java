package example;
import java.lang.reflect.Method;

import framework.Interceptor;

public class DaoInterceptor implements Interceptor {

    public Object handle(Object object, Method method, Object[] args, String interceptionAnnotationData) {
        System.out.println(interceptionAnnotationData);
        return new Employee((String) args[0]);
    }

}
