package exception;

public class DatabaseInconsistencyException extends RuntimeException {
    public DatabaseInconsistencyException(String message) {
        super(message);
    }
}
