package ro.fortech.allocation.technology.exception;

public class TechnologyNotFoundByExternalIdException extends RuntimeException {
    public TechnologyNotFoundByExternalIdException(String id) {
        super("Could not find technology with id: " + id);
    }
}
