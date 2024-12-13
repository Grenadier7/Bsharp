package BSharp.Commands.BasicCommands.Operators;

import BSharp.Exceptions.BSharpRuntimeException;
import BSharp.Commands.BasicCommands.Int.ConstantNumber;
import BSharp.Commands.NumberGetter;

/**
 * Acts like adding +1 to a number
 */
public class NOT extends Condition {

    NumberGetter statement;

    public NOT(Condition statement) {
        super(null);
        this.statement = statement;
    }

    @Override
    public Integer run() throws BSharpRuntimeException {
        return statement.run()-1;
    }

//    @Override
//    public ConstantString toConstantString() {
//        return null;
//    }

    @Override
    public ConstantNumber toConstantNumber() throws BSharpRuntimeException {
        return new ConstantNumber(this.run());
    }

    @Override
    public String toString() {
        return "!" + this.statement.toString();
    }
}
