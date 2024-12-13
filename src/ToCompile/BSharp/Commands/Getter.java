package ToCompile.BSharp.Commands;

import ToCompile.BSharp.Exceptions.BSharpRuntimeException;
import ToCompile.BSharp.Commands.BasicCommands.String.ConstantString;
import ToCompile.BSharp.Context;

public abstract class Getter extends Command {
    public Getter(Context context) {
        super(context);
    }

    public abstract Object run() throws BSharpRuntimeException;
    public ConstantString toConstantString() throws BSharpRuntimeException {
        return new ConstantString(String.valueOf(this.run()));
    }
}
