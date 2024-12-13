package ToCompile.BSharp.Commands;

import ToCompile.BSharp.Exceptions.CodeBlockStopCommunicator;
import ToCompile.BSharp.Context;

/**
 * During execution it communicates with the CodeBlock running it through "CodeBlockStopCommunicator"-Exceptions
 */
public class CodeBlockStop extends Command {
    CodeBlock codeBlockToStop = null;
    String functionName;

    public CodeBlockStop(Context context, String functionName) {
        super(context);
        this.functionName = functionName;
    }

    public CodeBlockStop(CodeBlock codeBlock) {
        super(null);
        this.codeBlockToStop = codeBlock;
    }

    @Override
    public Object run() throws CodeBlockStopCommunicator {
        if(codeBlockToStop == null) {
            codeBlockToStop = context.functionGet(functionName);
        }
        throw new CodeBlockStopCommunicator(codeBlockToStop);
    }

    @Override
    public String toString() {
        return "CodeBlock Stop " + (codeBlockToStop != null ? codeBlockToStop.toString() : functionName);
    }
}