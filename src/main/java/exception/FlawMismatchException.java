package exception;

public class FlawMismatchException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Flaw type has been resolved by the wrong operator or was given the wrong type of" +
                "consequences.";
    }
}
