## Synopsis

A Java-based POC for using AOP to implement logic for common methods


## Code Example

```
    // Declare abstract methods using @Intercept annotations and SpEL expression language
    public abstract class EmployeeDaoImpl implements EmployeeDao  {


        @Intercept("select * from employee where name='#{#name}'")
        public abstract Employee getByName(@Param("name") String n);

        // A normal method can use above annotated method!
        public boolean exist(String n) {
            return getByName(n) != null;
        }
    }

    // Implement Interceptor interface to handle the method calls for methods with @Intercept annotations
    public class DaoInterceptor implements Interceptor {

        public Object handle(Object object, Method method, Object[] args, String interceptionAnnotationData) {
            System.out.println(interceptionAnnotationData);
            return new Employee((String) args[0]);
        }

    }
    // Create objects from the abstract classes/interfaces and call abstract methods, Interceptor class with handle the call
    public static void main(String[] args) throws Exception {

        EmployeeDao d = ProxyFactory.createProxyObject(EmployeeDaoImpl.class, new DaoInterceptor());

        System.out.println(d.getByName("Peter").getName());
        System.out.println(d.exist("Peter"));

    }
```


## Motivation

To reduce the boilerplate code when implementing methods with similar logic.

When we need to implement multiple methods with similar logic like DAO methods, lookup methods, etc., we can just "declare" those methods using @Intercept annotation and implement the Interceptor interface to handle the method calls for those annotated methods (other methods without @Intercept annotation will not be intercepted by the Interceptor).

This pattern can be applied to many cases (Selenium locator methods, DAO methods, etc.). It is inspired by Spring Data project and is implemented to be more general for other cases.


## Installation

The classes under 'framework' package can be reused. The classes under 'example' are for illustration purpose.

## How it is implemented

The framework is implemented using CGLIB and Spring EL (SpEL). @Intercept and @Param annotations can be used together to generate strings to pass to the Interceptor class. CGLIB will help to generate a concrete class from the abstract class at the runtime, intercept the calls to annotated methods and pass the control to the Interceptor class

## License

Apache 2 license