package hu.syscode.users.Exception;

/**
 * Custom exception class to send to the client if something went wrong
 */
public class StudentException extends Exception {
    public StudentException(String errorMessage) {
        super(errorMessage);
    }
}