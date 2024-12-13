package ToCompile.BSharp.Commands.BasicCommands.String;

public class ConstantString extends StringOperation {

    String text;

    public ConstantString(String text) {
        super();
        this.text = text;
    }

    public String run() {
        return this.toString();
    }

    @Override
    public String toString() {
        return text;
    }
}

