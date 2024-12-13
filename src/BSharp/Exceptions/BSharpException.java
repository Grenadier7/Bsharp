package BSharp.Exceptions;

public class BSharpException extends Exception {
    public BSharpException() {
        super("B-SharpException");
    }

    public BSharpException(String message) {
        super(message);
    }

    public BSharpException(String message, Throwable cause) {
        super(message, cause);
    }
}
