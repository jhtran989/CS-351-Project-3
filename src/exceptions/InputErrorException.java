package exceptions;

/**
 * Throws an exception relating to input
 */
public class InputErrorException extends Exception{
    public InputErrorException() {
        super("Input error...");
    }
}
