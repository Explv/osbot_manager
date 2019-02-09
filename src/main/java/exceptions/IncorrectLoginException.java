package exceptions;

public class IncorrectLoginException extends Exception {
    public IncorrectLoginException() {
        super("Login details are incorrect");
    }
}
