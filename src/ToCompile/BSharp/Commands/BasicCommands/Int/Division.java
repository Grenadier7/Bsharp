package ToCompile.BSharp.Commands.BasicCommands.Int;

import ToCompile.BSharp.Exceptions.BSharpRuntimeException;
import ToCompile.BSharp.Commands.Getter;

public class Division extends Calculation {

    public Division(Getter firstNr, Getter secondNr) {
        super(new Getter[]{firstNr, secondNr}, "/");
    }


    @Override
    public Integer run() throws BSharpRuntimeException {
        return ((int) this.allNumbers[0].run()) / ((int) allNumbers[1].run());
    }

}
