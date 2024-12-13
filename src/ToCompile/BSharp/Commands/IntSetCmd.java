package ToCompile.BSharp.Commands;

import ToCompile.BSharp.Exceptions.BSharpRuntimeException;
import ToCompile.BSharp.Context;

public class IntSetCmd extends Command {
    private final NumberGetter number;
    private String variableName;
    public IntSetCmd(Context context, String variableName, NumberGetter number) {
        super(context);
        this.number = number;
        this.variableName = variableName;
    }



    @Override
    public Object run() throws BSharpRuntimeException {
        context.intVarsSet(variableName, number.toConstantNumber());
        return null;
    }

    @Override
    public String toString() {
        return "Int Set " + variableName + " to " + number.toString();
    }
}
