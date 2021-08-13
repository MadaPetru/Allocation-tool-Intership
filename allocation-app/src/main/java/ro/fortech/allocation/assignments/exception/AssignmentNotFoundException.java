package ro.fortech.allocation.assignments.exception;

public class AssignmentNotFoundException extends RuntimeException {
    public AssignmentNotFoundException(String uid) {
        super("Could not find assignment with Uid: " + uid);
    }
}
