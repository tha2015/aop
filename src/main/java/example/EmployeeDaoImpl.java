package example;

import framework.annotation.Intercept;
import framework.annotation.Param;

public abstract class EmployeeDaoImpl implements EmployeeDao  {

    public boolean exist(String n) {
        return getByName(n) != null;
    }

    @Intercept("select * from employee where name='#{#name}'")
    public abstract Employee getByName(@Param("name") String n);
}