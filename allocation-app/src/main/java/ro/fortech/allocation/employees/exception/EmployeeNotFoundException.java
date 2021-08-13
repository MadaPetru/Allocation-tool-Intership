package ro.fortech.allocation.employees.exception;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String uid) {
        super("Could not find employee with Uid: " + uid);
    }
}
