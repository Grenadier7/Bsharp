package Test;

import ToCompile.BSharp.Exceptions.BSharpRuntimeException;
import ToCompile.BSharp.Context;
import ToCompile.Backend.Core;

public class TestRun {
    public static void main(String[] args) {
        Context context = Core.readCodeFromFile(TestCompile.PATH_OUT);
        assert  context != null;
        try {
            context.functionGet("main").run();
        } catch (BSharpRuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
