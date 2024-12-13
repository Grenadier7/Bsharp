package BSharp.Commands.BasicCommands.Int;

import BSharp.Exceptions.BSharpRuntimeException;
import BSharp.Commands.Getter;

public class Division extends Calculation {

    public Division(Getter firstNr, Getter secondNr) {
        super(new Getter[]{firstNr, secondNr}, "/");
    }


    @Override
    public Integer run() throws BSharpRuntimeException {
        return ((int) this.allNumbers[0].run()) / ((int) allNumbers[1].run());
    }

}
