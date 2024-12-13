package BSharp.Commands.BasicCommands.Operators;

import BSharp.Exceptions.BSharpRuntimeException;
import BSharp.Commands.BasicCommands.Int.ConstantNumber;
import BSharp.Commands.NumberGetter;
import BSharp.Context;

public abstract class Condition extends NumberGetter {

    public Condition(Context context) {
        super(context);
    }

    @Override
    public abstract Integer run() throws BSharpRuntimeException;

    @Override
    public abstract ConstantNumber toConstantNumber() throws BSharpRuntimeException;


    @Override
    public abstract String toString();

    public int booleanToNumber(boolean b) {
        return b ? 1 : 0;
    }
    public boolean numberToBoolean(int n) {
        return n % 2 != 0;
    }

}
