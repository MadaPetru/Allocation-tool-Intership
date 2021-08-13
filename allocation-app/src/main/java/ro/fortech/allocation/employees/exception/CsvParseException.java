package ro.fortech.allocation.employees.exception;

public class CsvParseException extends RuntimeException {
    public CsvParseException(String message) {
        super("Fail to parse CSV file" + message);
    }
}
