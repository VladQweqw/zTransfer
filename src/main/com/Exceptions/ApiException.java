package main.com.Exceptions;

public class ApiException extends RuntimeException {
    public ApiException() {
        super("An error with the API occured, please try again later");
    }
}
