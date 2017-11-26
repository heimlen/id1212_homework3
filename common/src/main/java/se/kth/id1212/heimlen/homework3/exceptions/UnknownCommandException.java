package se.kth.id1212.heimlen.homework3.exceptions;

/**
 * Exception thrown when a user gives an unknown command to the interpreter.
 */
public class UnknownCommandException extends Throwable {
    public UnknownCommandException(String msg) {
        super(msg);
    }
}
