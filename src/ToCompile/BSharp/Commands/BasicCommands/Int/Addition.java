package ToCompile.BSharp.Commands.BasicCommands.Int;

import ToCompile.BSharp.Exceptions.BSharpRuntimeException;
import ToCompile.BSharp.Commands.Getter;

public class Addition extends Calculation {

    public Addition(Getter firstNr, Getter secondNr) {
        this(new Getter[]{firstNr, secondNr});
    }
    public Addition(Getter firstNr, Getter secondNr, Getter thirdNr) {
        this(new Getter[]{firstNr, secondNr, thirdNr});
    }
    public Addition(Getter[] allNumbers) {
        super(allNumbers, "+");
    }

    @Override
    public Integer run() throws BSharpRuntimeException {
        int result = 0;
        for (Getter cmd : allNumbers) {
            result += (int) cmd.run();
        }
        return result;
    }


}
