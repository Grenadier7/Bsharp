package ToCompile.BSharp.Commands.BasicCommands.Int;

import ToCompile.BSharp.Exceptions.BSharpRuntimeException;
import ToCompile.BSharp.Commands.Getter;
import ToCompile.BSharp.Commands.NumberGetter;

public abstract class Calculation extends NumberGetter {
    protected Getter[] allNumbers;

    protected String symbol;

    public Calculation(Getter[] allNumbers, String symbol) {
        super(null);
        this.allNumbers = allNumbers;
        this.symbol = symbol;
        throwErrorIfNotAllAreIntegers();
    }
    public Calculation() {
        super(null);
    }

    private void throwErrorIfNotAllAreIntegers() throws NumberFormatException {
        for(Getter cmd : allNumbers) {
            if(!(cmd instanceof NumberGetter)) throw new NumberFormatException(cmd.toString() + " is unable to return a number");
        }
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public abstract Integer run() throws BSharpRuntimeException;

    @Override
    public String toString() {
        String result = "(";
        for (Getter cmd : allNumbers) {
            result += cmd.toString() + " " + symbol;
        }
        result = result.substring(0, result.length()-2);
        result += ")";
        return result;
    }
    @Override
    public ConstantNumber toConstantNumber() throws BSharpRuntimeException {
        return new ConstantNumber(this.run());
    }
}
