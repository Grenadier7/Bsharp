package BSharp.Commands;

import BSharp.Exceptions.BSharpRuntimeException;
import BSharp.Commands.BasicCommands.Operators.Condition;
import BSharp.Context;

public class If extends Command {

    NumberGetter condition;
    String name;
    Function ifBody;

    public If(Context context, String name, Condition condition, Function ifBody) {
        super(context);
        this.condition = condition;
        this.name = name;
        this.ifBody = ifBody;
    }
    @Override
    public Object run() throws BSharpRuntimeException {
        if (If.numberToBoolean(condition.run())) {
            context.ifWasTrueSet(name, true);
            ifBody.run();
        } else {
            context.ifWasTrueSet(name, false);
        }
        return null;
    }

    @Override
    public String toString() {
        return "If (" + condition + ")" + ifBody;
    }


    private static boolean numberToBoolean(int number) {
        if(number % 2 == 0) return false;
        return true;
    }
}
