package ToCompile.BSharp.Commands.BasicCommands.Int;

import ToCompile.BSharp.Exceptions.BSharpRuntimeException;
import ToCompile.BSharp.Commands.Getter;

public class Subtraction extends Calculation {

    public Subtraction(Getter positiveNumber, Getter negativNumber) {
        this(new Getter[]{positiveNumber, negativNumber});
    }
    public Subtraction(Getter positiveNumber, Getter negativNumber1, Getter negativNumber2) {
        this(new Getter[]{positiveNumber, negativNumber1, negativNumber2});
    }
    public Subtraction(Getter[] allNumbers) {
        super(allNumbers, "-");
    }

    @Override
    public Integer run() throws BSharpRuntimeException {
        int result = (int) allNumbers[0].run();
        for (int i = 1; i < allNumbers.length; i++) {
            result -= (int) allNumbers[i].run();
        }
        return result;
    }

}
