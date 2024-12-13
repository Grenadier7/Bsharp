package BSharp.Commands.BasicCommands.String;

import BSharp.Exceptions.BSharpRuntimeException;
import BSharp.Commands.Command;
import BSharp.Commands.Getter;
import BSharp.Commands.StringGetter;

public abstract class StringOperation extends StringGetter {
    protected Command[] allStrings;

    protected String symbol;

    public StringOperation(Command[] allStrings, String symbol) {
        super(null);
        this.allStrings = allStrings;
        this.symbol = symbol;
        throwErrorIfNotAllAreGetters();
    }
    public StringOperation() {
        super(null);
    }

    private void throwErrorIfNotAllAreGetters() throws NumberFormatException {
        for(Command cmd : allStrings) {
            if(!(cmd instanceof Getter)) throw new NumberFormatException(cmd.toString() + " is unable to return a Text");
        }
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public abstract String run() throws BSharpRuntimeException;

    @Override
    public String toString() {
        String result = "(";
        for (Command cmd : allStrings) {
            result += cmd.toString() + " " + symbol;
        }
        result = result.substring(0, result.length()-2);
        result += ")";
        return result;
    }
}
