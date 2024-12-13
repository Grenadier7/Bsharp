package BSharp.Commands;

import BSharp.Exceptions.BSharpRuntimeException;
import BSharp.Commands.BasicCommands.Int.ConstantNumber;
import BSharp.Context;

public abstract class NumberGetter extends Getter {
    public NumberGetter(Context context) {
        super(context);
    }

    public abstract Integer run() throws BSharpRuntimeException;
    public abstract ConstantNumber toConstantNumber() throws BSharpRuntimeException;
}
