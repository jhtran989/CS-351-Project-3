package exceptions;

public class InternalError extends Exception {
    public InternalError(String message) {
        super("Internal error..." + message);
    }
}
