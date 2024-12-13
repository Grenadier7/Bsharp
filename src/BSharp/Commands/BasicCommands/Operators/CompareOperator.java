package BSharp.Commands.BasicCommands.Operators;

import BSharp.Exceptions.BSharpException;
import BSharp.Exceptions.BSharpRuntimeException;
import BSharp.Commands.BasicCommands.Int.ConstantNumber;
import BSharp.Commands.Getter;
import BSharp.Commands.NumberGetter;
import BSharp.Commands.StringGetter;

/**
 * Compares two Getters and returns a number
 */
public class CompareOperator extends Condition {

    public enum Operator {
        EQUALS("=="), NOT_EQUALS("!="), GREATER(">"), GREATER_OR_EQUAL(">="), LESS("<"), LESS_OR_EQUAL("<=");
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


    public CompareOperator(Getter var1, Getter var2, Operator operator) throws BSharpException {
        super(null);
        //checks if both numbers have the same variable types
        if((var1 instanceof NumberGetter && var2 instanceof StringGetter) || (var1 instanceof StringGetter && var2 instanceof NumberGetter)) throw new BSharpException("Variable return type of \"" + var1 + "\" and \"" + var2 + "\" do not match");

        //prevents all Operator other than Equals or not Equals to be used on Strings
        switch (operator) {
            case GREATER, GREATER_OR_EQUAL, LESS, LESS_OR_EQUAL -> {
                if(var1 instanceof StringGetter || var2 instanceof StringGetter) throw new BSharpException("Operator \"" + operator.getSymbol() + "\" cannot be applied to String");
            }
        }

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
        if(var1 instanceof StringGetter) {
            switch(operator) {
                case EQUALS -> {
                    return super.booleanToNumber(var1.run().equals(var2.run()));
                }
                case NOT_EQUALS -> {
                    return super.booleanToNumber(!var1.run().equals(var2.run()));
                }
                default -> {
                    throw new RuntimeException("UNREACHABLE");
                }
            }
        }
        if(var1 instanceof NumberGetter) {
            switch(operator) {
                case EQUALS -> {
                    return super.booleanToNumber((int) var1.run() == (int) var2.run());
                }
                case NOT_EQUALS -> {
                    return super.booleanToNumber((int) var1.run() != (int) var2.run());
                }
                case GREATER -> {
                    return super.booleanToNumber((int) var1.run() > (int) var2.run());
                }
                case GREATER_OR_EQUAL -> {
                    return super.booleanToNumber((int) var1.run() >= (int) var2.run());
                }
                case LESS -> {
                    return super.booleanToNumber((int) var1.run() < (int) var2.run());
                }
                case LESS_OR_EQUAL -> {
                    return super.booleanToNumber((int) var1.run() <= (int) var2.run());
                }
                default -> {
                    throw new RuntimeException("UNREACHABLE");
                }
            }
        }
        throw new RuntimeException("UNREACHABLE");
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
