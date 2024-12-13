package ToCompile.BSharp.Commands;

import ToCompile.BSharp.Context;

/**
 * A user-callable Code-Block
 */
public class Function extends CodeBlock{
    String functionName;

    public Function(Context context, String name, Command[] functionCommands) {
        super(context, functionCommands);
        this.functionName = name;
        context.functionSet(name, this);
    }

    @Override
    public String toString() {
        return "Function " + functionName + " " + super.toString();
    }
}
