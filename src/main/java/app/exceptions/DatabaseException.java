package app.exceptions;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
        System.out.println("userMessage: " + message);
    }

    public DatabaseException(String userMessage, String systemMessage)
    {
        super(userMessage);
        System.out.println("userMessage: " + userMessage);
        System.out.println("errorMessage: " + systemMessage);
    }
}
