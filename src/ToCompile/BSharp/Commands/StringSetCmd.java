package ToCompile.BSharp.Commands;

import ToCompile.BSharp.Exceptions.BSharpRuntimeException;
import ToCompile.BSharp.Context;

public class StringSetCmd extends Command {
    private final StringGetter text;
    private String variableName;
    public StringSetCmd(Context context, String variableName, StringGetter text) {
        super(context);
        this.text = text;
        this.variableName = variableName;
    }



    @Override
    public Object run() throws BSharpRuntimeException {
        context.StringVarsSet(variableName, text.toConstantString());
        return null;
    }

    @Override
    public String toString() {
        return "String Set " + variableName;
    }
}
