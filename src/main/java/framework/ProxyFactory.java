package framework;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import framework.impl.ProxyObjectMethodImpl;

public class ProxyFactory {
    @SuppressWarnings("unchecked")
    public static <T> T createProxyObject(Class<T> clazz, Interceptor handler) {
        MethodInterceptor methodInterceptor = new ProxyObjectMethodImpl(handler);
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(methodInterceptor);
        return (T) enhancer.create();
    }
}
