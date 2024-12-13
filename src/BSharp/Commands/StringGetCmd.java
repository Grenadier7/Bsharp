package BSharp.Commands;

import BSharp.Commands.BasicCommands.Int.ConstantNumber;
import BSharp.Commands.BasicCommands.String.ConstantString;
import BSharp.Context;

public class StringGetCmd extends StringGetter{

    private String variableName;
    public StringGetCmd(Context context, String varName) {
        super(context);
        variableName = varName;
    }

    @Override
    public String run() {
         return context.StringVarsGet(variableName) != null ? context.StringVarsGet(variableName).run() : "";
    }

    @Override
    public String toString() {
        return "String Get " + variableName;
    }
}
