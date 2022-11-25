package exception;

public class EmptyClauseException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Empty clause has been reached";
    }
}
