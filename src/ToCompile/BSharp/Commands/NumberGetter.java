package ToCompile.BSharp.Commands;

import ToCompile.BSharp.Exceptions.BSharpRuntimeException;
import ToCompile.BSharp.Commands.BasicCommands.Int.ConstantNumber;
import ToCompile.BSharp.Context;

public abstract class NumberGetter extends Getter {
    public NumberGetter(Context context) {
        super(context);
    }

    public abstract Integer run() throws BSharpRuntimeException;
    public abstract ConstantNumber toConstantNumber() throws BSharpRuntimeException;
}
