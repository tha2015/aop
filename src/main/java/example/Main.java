package example;

import framework.ProxyFactory;

public class Main {

    public static void main(String[] args) throws Exception {

        EmployeeDao d = ProxyFactory.createProxyObject(EmployeeDaoImpl.class, new DaoInterceptor());

        System.out.println(d.getByName("Peter").getName());
        System.out.println(d.exist("Peter"));

    }
}
