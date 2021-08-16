package ro.fortech.allocation.project.exception;

public class ProjectNotFoundException extends RuntimeException{
    public ProjectNotFoundException(String externalId) {
        super("Could not find project with externalId: " + externalId);
    }
}
