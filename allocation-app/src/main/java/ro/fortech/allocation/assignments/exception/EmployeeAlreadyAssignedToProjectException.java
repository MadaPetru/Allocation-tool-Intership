package ro.fortech.allocation.assignments.exception;

public class EmployeeAlreadyAssignedToProjectException extends RuntimeException {
    public EmployeeAlreadyAssignedToProjectException(String employeeUid, String projectUid) {
        super("Employee with Uid: " + employeeUid + "is already assigned, in that period, to project with uid: " + projectUid);
    }
}
