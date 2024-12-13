package BSharp.Commands;

import BSharp.Exceptions.BSharpRuntimeException;
import BSharp.Commands.BasicCommands.String.ConstantString;
import BSharp.Context;

public abstract class Getter extends Command {
    public Getter(Context context) {
        super(context);
    }

    public abstract Object run() throws BSharpRuntimeException;
    public ConstantString toConstantString() throws BSharpRuntimeException {
        return new ConstantString(String.valueOf(this.run()));
    }
}
