package example;

public interface EmployeeDao {
    boolean exist(String n);
    Employee getByName(String n);
}