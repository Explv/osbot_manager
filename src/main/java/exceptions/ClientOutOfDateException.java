package exceptions;

public class ClientOutOfDateException extends Exception {
    public ClientOutOfDateException() {
        super("OSBot client is out of date");
    }
}
