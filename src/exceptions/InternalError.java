package exceptions;

/**
 * More of a debugging tool to see if something went wrong (used if the board
 * configuration was not valid)
 */
public class InternalError extends Exception {
    public InternalError(String message) {
        super("Internal error..." + message);
    }
}
