package BSharp.Commands.BasicCommands.Int;

import BSharp.Exceptions.BSharpRuntimeException;
import BSharp.Commands.Command;
import BSharp.Commands.Getter;

public class Multiplication extends Calculation {

    public Multiplication(Getter firstNr, Getter secondNr) {
        this(new Getter[]{firstNr, secondNr});
    }
    public Multiplication(Getter firstNr, Getter secondNr, Getter thirdNr) {
        this(new Getter[]{firstNr, secondNr, thirdNr});
    }
    public Multiplication(Getter[] allNumbers) {
        super(allNumbers, "*");
    }

    public Multiplication(ConstantNumber constantNumber) {
    }

    public Multiplication(Getter part1, Command part2) {
    }

    @Override
    public Integer run() throws BSharpRuntimeException {
        int result = (int) allNumbers[0].run();
        for (int i = 1; i < allNumbers.length; i++) {
            result *= (int) allNumbers[i].run();
        }
        return result;
    }

}
