package BSharp.Commands;

import BSharp.Exceptions.BSharpRuntimeException;
import BSharp.Commands.BasicCommands.Int.ConstantNumber;

public class AsNumberGetter extends NumberGetter {
    Command cmd;
    public AsNumberGetter(Command cmd) {
        super(null);
        this.cmd = cmd;
    }

    @Override
    public Integer run() throws BSharpRuntimeException {
        if(cmd instanceof NumberGetter) return ((NumberGetter)cmd).run();
        if(cmd instanceof Getter) {
            int number;
            try {
                number = Integer.parseInt(cmd.run().toString());
                return number;
            }
            catch (NumberFormatException e) {
                throw new BSharpRuntimeException(cmd.run().toString() + " cannot be cast to an integer");
            }
        }
        throw new BSharpRuntimeException(cmd.toString() + " does not return a value");
    }

    @Override
    public String toString() {
        return "To Number: " + cmd.toString();
    }

    @Override
    public ConstantNumber toConstantNumber() throws BSharpRuntimeException {
        return new ConstantNumber(this.run());
    }
}
