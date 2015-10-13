package framework.impl;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import framework.Interceptor;
import framework.annotation.Intercept;
import framework.annotation.Param;

public class ProxyObjectMethodImpl implements MethodInterceptor {
    private Interceptor interceptor;

    public ProxyObjectMethodImpl(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
        Intercept methodData = AnnotationUtils.findAnnotation(bridgedMethod, Intercept.class);

        if (methodData == null) {
            return methodProxy.invokeSuper(proxy, args);
        } else {
            String interceptionAnnotationData = methodData.value();
            String[] paramNames = resolveParamNames(proxy, method, args, interceptionAnnotationData);
            interceptionAnnotationData = resolveExpression(proxy, method, args, interceptionAnnotationData, paramNames);
            return interceptor.handle(proxy, method, args, interceptionAnnotationData);
        }
    }

    private String[] resolveParamNames(Object proxy, Method method, Object[] args, String interceptionAnnotationData) {
        String[] names = new String[method.getParameterCount()];
        for (int i = 0; i < names.length; i++) {
            MethodParameter methodParam = new MethodParameter(method, i);
            Param paramAnno = methodParam.getParameterAnnotation(Param.class);
            if (paramAnno != null) names[i] = paramAnno.value();
        }
        return names;
    }

    private String resolveExpression(Object proxy, Method method, Object[] args, String exp, String[] paramNames) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setRootObject(proxy);
        for (int i = 0; i < paramNames.length; i++) {
            if (paramNames[i] != null) {
                context.setVariable(paramNames[i], args[i]);
            }
        }
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(exp, new TemplateParserContext());
        Object value = expression.getValue(context, Object.class);
        return value.toString();
    }

}
