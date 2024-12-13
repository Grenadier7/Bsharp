package BSharp.Exceptions;

import BSharp.Commands.CodeBlock;

public class CodeBlockStopCommunicator extends RuntimeException {
    CodeBlock codeBlock;
    public CodeBlockStopCommunicator(CodeBlock codeBlock) {
        super("Stop " + codeBlock.toString());
        this.codeBlock = codeBlock;
    }
    public CodeBlock getCodeBlock() {
        return codeBlock;
    }
}
