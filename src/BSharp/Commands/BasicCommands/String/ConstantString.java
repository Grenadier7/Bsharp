package BSharp.Commands.BasicCommands.String;

import BSharp.Commands.Command;
import BSharp.Commands.Getter;

public class ConstantString extends StringOperation {

    String text;

    public ConstantString(String text) {
        super();
        this.text = text;
    }

    public String run() {
        return this.toString();
    }

    @Override
    public String toString() {
        return text;
    }
}

