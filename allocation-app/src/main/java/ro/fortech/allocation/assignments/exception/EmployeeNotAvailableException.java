package ro.fortech.allocation.assignments.exception;

public class EmployeeNotAvailableException extends RuntimeException {
    public EmployeeNotAvailableException(String employeeUid) {
        super("Employee with Uid: " + employeeUid + " would exceed his allocated working hours for that period of time.");//"is not available to work.");
    }
}
