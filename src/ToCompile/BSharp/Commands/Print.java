package ToCompile.BSharp.Commands;

import ToCompile.BSharp.Exceptions.BSharpRuntimeException;
import ToCompile.BSharp.Context;

public class Print extends Command {
    Getter text;
    public Print(Context context, Getter text) {
        super(context);
        this.text = text;
    }

    @Override
    public Object run() throws BSharpRuntimeException {
        context.printOut(String.valueOf(text.run()));
        return null;
    }

    @Override
    public String toString() {
        return "Print " + text.toString();
    }
}
