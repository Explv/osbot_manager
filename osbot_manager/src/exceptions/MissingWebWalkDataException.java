package exceptions;

public class MissingWebWalkDataException extends Exception {
    public MissingWebWalkDataException() {
        super("Missing web walking data.\nDownload the data by running OSBot manually.");
    }
}
