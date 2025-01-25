package main.com.Exceptions;

public class NotFound extends RuntimeException {
    public NotFound() {
        super("Endpoint not found");
    }
}
