package se.kth.id1212.heimlen.homework3.exceptions;

/**
 * This exception is thrown if a user fails to enter a correctly formatted input.
 */
public class BadFormattedInputException extends Throwable {
    public BadFormattedInputException(String msg) {
        super(msg);
    }
}
