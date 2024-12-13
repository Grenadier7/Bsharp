package ToCompile.BSharp.Commands;

import ToCompile.BSharp.Exceptions.BSharpRuntimeException;
import ToCompile.BSharp.Context;

public class Input extends StringGetter {
    Getter displayText;
    public Input(Context context, Getter displayText) {
        super(context);
        this.displayText = displayText;
    }

    @Override
    public String run() throws BSharpRuntimeException {
        return context.readIn(displayText.run().toString());
    }

    @Override
    public String toString() {
        return "Input \"" + displayText.toString() + "\"";
    }

}
