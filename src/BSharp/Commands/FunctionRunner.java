package BSharp.Commands;

import BSharp.Exceptions.BSharpRuntimeException;
import BSharp.Context;

public class FunctionRunner extends Command {
    String functionName;
    public FunctionRunner(Context context, String functionName) {
        super(context);
        this.functionName = functionName;
    }

    @Override
    public Object run() throws BSharpRuntimeException {
        context.functionGet(functionName).run();
        return null;
    }

    @Override
    public String toString() {
        return "Function Run " + functionName;
    }
}
