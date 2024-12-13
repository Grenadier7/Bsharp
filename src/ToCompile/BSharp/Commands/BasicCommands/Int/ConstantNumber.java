package ToCompile.BSharp.Commands.BasicCommands.Int;

/**
 * A class to save a number inside of a command
 */
public class ConstantNumber extends Calculation {
    int nr;
    public ConstantNumber(int number) {
        super();
        this.nr = number;
    }
    public Integer run() {
        return nr;
    }

    @Override
    public String toString() {
        return "" + nr;
    }
}
