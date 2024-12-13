package ToCompile.BSharp.Exceptions;

public class CompileError extends BSharpException {
    public CompileError() {
        super("B-SharpException");
    }

    public CompileError(String message) {
        super(message);
    }

    public CompileError(String message, Throwable cause) {
        super(message, cause);
    }
}
