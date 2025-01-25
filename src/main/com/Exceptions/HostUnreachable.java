package main.com.Exceptions;

public class HostUnreachable extends RuntimeException {
    public HostUnreachable() {
        super("API endpoint cannot be reached, possible causes:\n -> API down \n -> No internet \n -> bad code");
    }
}
