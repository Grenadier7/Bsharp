package ToCompile.BSharp.Commands;

import ToCompile.BSharp.Commands.BasicCommands.Int.ConstantNumber;
import ToCompile.BSharp.Context;

public class IntGetCmd extends NumberGetter{

    String variableName;

    public IntGetCmd(Context context, String varName) {
        super(context);
        this.variableName = varName;
    }

    @Override
    public Integer run() {
         return context.intVarsGet(variableName) != null ? context.intVarsGet(variableName).run() : 0;
    }

    @Override
    public ConstantNumber toConstantNumber() {
        return new ConstantNumber(this.run());
    }

    @Override
    public String toString() {
        return "Int Get " + variableName;
    }

}
