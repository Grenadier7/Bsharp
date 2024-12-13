package BSharp;

import BSharp.Commands.*;
import BSharp.Commands.BasicCommands.Int.ConstantNumber;
import BSharp.Commands.BasicCommands.String.ConstantString;

import javax.swing.*;
import java.io.PrintStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.PrimitiveIterator;
import java.util.Scanner;

public class Context implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // Recommended for Serializable classes
    private HashMap<String, ConstantNumber> intVars = new HashMap<>();
    private HashMap<String, ConstantString> stringVars = new HashMap<>();
    private HashMap<String, CodeBlock> functions = new HashMap<>();
    private HashMap<String, Boolean> ifWasTrue = new HashMap<>();

    private PrintStreamType out = PrintStreamType.JOPTIONPANE_OUT;
    private PrintStreamType err = PrintStreamType.SYSTEM_ERR;
    private InputStreamType in = InputStreamType.JOPTIONPANE_IN;
    private boolean isConfigured = false;


    private void print(String s, PrintStreamType out) {
        switch (out) {
            case SYSTEM_OUT:
                System.out.println(s);
                break;
            case SYSTEM_ERR:
                System.err.println(s);
                break;
            case JOPTIONPANE_OUT:
                JOptionPane.showMessageDialog(null, s, "Programm Out", JOptionPane.INFORMATION_MESSAGE);
                break;
            case JOPTIONPANE_ERR:
                JOptionPane.showMessageDialog(null, s, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void config(PrintStreamType out, PrintStreamType err, InputStreamType in) {
        if(!isConfigured) {
            //uses the already set defaults if values are null
            this.out = out != null ? out : this.out;
            this.err = err != null ? err : this.err;
            this.in = in != null ? in : this.in;
        } else {
            throw new IllegalStateException("Context is already configured");
        }

    }

    public void printOut(String s) {
        print(s, out);
    }

    public void printErr(String s) {
        print(s, err);
    }

    public String readIn(String message) {
        switch (in) {
            case SYSTEM_IN:
                System.out.println(message);
                Scanner sc = new Scanner(System.in);
                return sc.nextLine();
            case JOPTIONPANE_IN:
                return JOptionPane.showInputDialog(null, message, "Programm In", JOptionPane.INFORMATION_MESSAGE);
        }
        throw new RuntimeException("Unreachable");
    }


    public Context(PrintStreamType out, PrintStreamType err, InputStreamType in) {
        this.out = out;
        this.err = err;
        this.in = in;
    }

    public Context() {
    }


    public ConstantNumber intVarsGet(String varName) {
        return intVars.get(varName);
    }

    public ConstantString StringVarsGet(String varName) {
        return stringVars.get(varName);
    }

    public CodeBlock functionGet(String functionName) {
        return functions.get(functionName);
    }

    public Boolean ifWasTrueGet(String varName) {
        return ifWasTrue.get(varName);
    }

    public void intVarsSet(String varName, ConstantNumber value) {
        intVars.put(varName, value);
    }

    public void StringVarsSet(String varName, ConstantString value) {
        stringVars.put(varName, value);
    }

    public void functionSet(String functionName, CodeBlock codeBlock) {
        functions.put(functionName, codeBlock);
    }

    public void ifWasTrueSet(String varName, Boolean value) {
        ifWasTrue.put(varName, value);
    }

    public boolean hasFunction(String functionName) {
        return functions.containsKey(functionName);
    }


    public enum PrintStreamType {
        SYSTEM_OUT, SYSTEM_ERR, JOPTIONPANE_OUT, JOPTIONPANE_ERR;
    }

    public enum InputStreamType {
        SYSTEM_IN, JOPTIONPANE_IN;
    }

}
