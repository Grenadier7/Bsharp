package ToCompile.BSharp.Commands;

import ToCompile.BSharp.Exceptions.BSharpRuntimeException;
import ToCompile.BSharp.Context;

public class Else extends Command {

    String name;
    Function elseBody;

    public Else(Context context, String name, Function elseBody) {
        super(context);
        this.name = name;
        this.elseBody = elseBody;
    }
    @Override
    public Object run() throws BSharpRuntimeException {
        Boolean ifWasTrue = context.ifWasTrueGet(name);
        if(ifWasTrue == null || ifWasTrue) return null;
        elseBody.run();
        return null;
    }

    @Override
    public String toString() {
        return "Else" + elseBody.toString();
    }
}

