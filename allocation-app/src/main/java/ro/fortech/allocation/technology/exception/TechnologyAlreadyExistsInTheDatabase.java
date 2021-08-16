package ro.fortech.allocation.technology.exception;

public class TechnologyAlreadyExistsInTheDatabase extends RuntimeException {
    public TechnologyAlreadyExistsInTheDatabase(String dtoTechnologyName, String technologyName) {
        super("Technology with the name " + dtoTechnologyName + " already exists but is saved as " + technologyName);
    }
}