package BSharp.Commands.BasicCommands.Operators;

import BSharp.Exceptions.BSharpException;
import BSharp.Exceptions.BSharpRuntimeException;
import BSharp.Commands.BasicCommands.Int.ConstantNumber;
import BSharp.Commands.Getter;
import BSharp.Commands.NumberGetter;

/**
 * Compares two Getters and returns a number
 */
public class ConnectOperator extends Condition {

    public enum Operator {
        AND("&&"), OR("||"), XOR("^");
        private String symbol;
        Operator(String symbol) {
            this.symbol = symbol;
        }
        public String getSymbol() {
            return symbol;
        }
    }

    private Getter var1;
    private Getter var2;
    private Operator operator;


    public ConnectOperator(Getter var1, Getter var2, Operator operator) throws BSharpException {
        super(null);
        //checks if both numbers have the same variable types
        if(!(var1 instanceof NumberGetter) || !(var2 instanceof NumberGetter)) throw new BSharpException("Both variables(\"" + var1 + "\" and \"" + var2 + "\") need to be Conditions!");

        this.var1 = var1;
        this.var2 = var2;
        this.operator = operator;

    }


    /**
     * Returns an uneven number if the statement is true and an even number if it is false
     * @return an uneven number if the statement is true and an even number if it is false
     */
    @Override
    public Integer run() throws BSharpRuntimeException {
            switch(operator) {
                case AND -> {
                    return super.booleanToNumber(super.numberToBoolean((int) var1.run()) && super.numberToBoolean((int) var2.run()));
                }
                case OR -> {
                    return super.booleanToNumber(super.numberToBoolean((int) var1.run()) || super.numberToBoolean((int) var2.run()));
                }
                case XOR -> {
                    return super.booleanToNumber(super.numberToBoolean((int) var1.run()) ^ super.numberToBoolean((int) var2.run()));
                }
                default -> {
                    throw new RuntimeException("UNREACHABLE");
                }
            }
    }

    @Override
    public String toString() {
        return var1.toString() + " " + operator.getSymbol() + " " + var2.toString();
    }

    @Override
    public ConstantNumber toConstantNumber() throws BSharpRuntimeException {
        return new ConstantNumber(this.run());
    }

//    @Override
//    public ConstantString toConstantString() {
//        return new ConstantString(this.toString());
//    }
}
