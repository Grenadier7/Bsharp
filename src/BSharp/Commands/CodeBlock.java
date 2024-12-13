package BSharp.Commands;

import BSharp.Exceptions.BSharpRuntimeException;
import BSharp.Exceptions.CodeBlockStopCommunicator;
import BSharp.Context;

/**
 * A Code Segment containing multiple lines of code
 * It can be run to run all the commands inside of it
 * All backend encapsulation (like ifs) is stores using CodeBlocks
 */
public class CodeBlock extends Command {
    private final Command[] functionCommands;


    public CodeBlock(Context context, Command[] functionCommands) {
        super(context);
        this.functionCommands = functionCommands;
    }

    //#ToDo Remove Fucntion
    public Command[] getFunctionCommands() {
        return functionCommands;
    }

    /**
     * Runs a code Block until it reaches the end or gets to a stop
     * @return nothing
     * @throws CodeBlockStopCommunicator throws the exception if the exception is meant for a higher Code Block containing this one
     */
    @Override
    public Object run() throws CodeBlockStopCommunicator, BSharpRuntimeException {
        for(Command functionCommand : functionCommands) {
            try {
                functionCommand.run();
            } catch (CodeBlockStopCommunicator e) {
                if(e.getCodeBlock() == null) continue;
                if(e.getCodeBlock().equals(this)) return null;
                throw e;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Function";

//        StringBuilder out;
//            out = new StringBuilder("{ \n");
//            for(Command functionCommand : functionCommands) {
//                out.append(functionCommand.toString()).append("\n");
//            }
//        out.append("}");
//        return out.toString();

    }
}
