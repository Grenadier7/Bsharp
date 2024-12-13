package ToCompile.Backend;

import ToCompile.BSharp.Context;
import ToCompile.BSharp.Exceptions.BSharpRuntimeException;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ClassFileMain {
    public static void main(String[] args) throws IOException, BSharpRuntimeException {
        Context context = Core.readCodeFromStream(new ObjectInputStream(ClassFileMain.class.getResourceAsStream("code/runnableCode.bsc")));
        if(context == null) throw new BSharpRuntimeException("Could not read code");
        context.functionGet("main").run();


    }
}
