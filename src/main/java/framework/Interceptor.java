package framework;

import java.lang.reflect.Method;

public interface Interceptor {
    Object handle(Object object, Method method, Object[] args, String interceptionAnnotationData);
}
