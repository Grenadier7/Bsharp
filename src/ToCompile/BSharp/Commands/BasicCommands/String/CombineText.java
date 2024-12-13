package ToCompile.BSharp.Commands.BasicCommands.String;

import ToCompile.BSharp.Exceptions.BSharpRuntimeException;
import ToCompile.BSharp.Commands.Command;

public class CombineText extends StringOperation {

    public CombineText(Command firstString, Command secondString) {
        this(new Command[]{firstString, secondString});
    }
    public CombineText(Command firstString, Command secondString, Command thirdString) {
        this(new Command[]{firstString, secondString, thirdString});
    }
    public CombineText(Command[] allStrings) {
        super(allStrings, "+");
    }

    @Override
    public String run() throws BSharpRuntimeException {
        String result = "";
        for (Command cmd : allStrings) {
            result += cmd.run().toString();
        }
        return result;
    }

}
