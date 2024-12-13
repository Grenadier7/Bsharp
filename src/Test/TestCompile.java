package Test;

import BSharp.Exceptions.BSharpException;
import BSharp.*;
import BSharp.Context;
import Backend.Core;

import java.io.File;

public class TestCompile {
    public static final String PATH = "src/testFiles/";
    public static final String PATH_OUT = PATH + "out/code.cbs/";
    public static void main(String[] args) throws BSharpException {

        Context context = Lexer.readIn(new File(PATH + "in/code.bs"));

        Core.compileCodetoClassFile(PATH_OUT, context, "main");

    }

}