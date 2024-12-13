package ToCompile.BSharp;

import ToCompile.BSharp.Commands.*;
import ToCompile.BSharp.Commands.BasicCommands.Int.*;
import ToCompile.BSharp.Commands.BasicCommands.Operators.CompareOperator;
import ToCompile.BSharp.Commands.BasicCommands.Operators.Condition;
import ToCompile.BSharp.Commands.BasicCommands.Operators.ConnectOperator;
import ToCompile.BSharp.Commands.BasicCommands.String.CombineText;
import ToCompile.BSharp.Commands.BasicCommands.String.ConstantString;
import ToCompile.BSharp.Exceptions.BSharpException;
import ToCompile.BSharp.Exceptions.CompileError;

import java.io.*;
import java.util.*;

public class Lexer {

    public static class LexerEndBlock extends Exception {
        private Keyword blockType;
        private String blockName;

        public LexerEndBlock(Keyword blockType, String blockName) {
            super("Lexer end Block \"" + blockType + " " + blockName + "\"");
            this.blockType = blockType;
            this.blockName = blockName;
        }

        public Keyword getBlockType() {
            return blockType;
        }

        public String getBlockName() {
            return blockName;
        }
    }

    public enum Keyword {
        FUNCTION(KeywordType.STARTING_WORD),
        INT(new KeywordType[]{KeywordType.STARTING_WORD, KeywordType.VARIABLE_TYPE_WORD}),
        STRING(new KeywordType[]{KeywordType.STARTING_WORD, KeywordType.VARIABLE_TYPE_WORD}),
        PAR(KeywordType.STARTING_WORD),
        IF(new KeywordType[]{KeywordType.STARTING_WORD, KeywordType.BLOCK_STARTER}),
        ELSE(new KeywordType[]{KeywordType.STARTING_WORD, KeywordType.BLOCK_STARTER}),
        PRINT(KeywordType.STARTING_WORD), INPUT(KeywordType.STARTING_WORD), CONFIG(KeywordType.STARTING_WORD),

        START(KeywordType.HELP_WORD),
        STOP(KeywordType.HELP_WORD),
        END(KeywordType.HELP_WORD),
        RUN(KeywordType.HELP_WORD),
        GET(KeywordType.HELP_WORD),
        SET(KeywordType.HELP_WORD),

        WHEN(new KeywordType[0]);


        private KeywordType[] types;

        Keyword(KeywordType[] types) {
            this.types = types;
        }

        Keyword(KeywordType type) {
            types = new KeywordType[]{type};
        }

        public boolean isType(KeywordType type) {
            for (KeywordType t : types) {
                if (t.equals(type)) return true;
            }
            return false;
        }

        public static Keyword getKeyword(String text) {
            switch (text) {
                case "F":
                    return FUNCTION;
                case "Int":
                    return INT;
                case "String":
                    return STRING;
                case "If":
                    return IF;
                case "Else":
                    return ELSE;
                case "Print":
                    return PRINT;
                case "Start":
                    return START;
                case "Stop":
                    return STOP;
                case "End":
                    return END;
                case "Run":
                    return RUN;
                case "Get":
                    return GET;
                case "Set":
                    return SET;
                case "When":
                    return WHEN;
                case "Input":
                    return INPUT;
                case "Config":
                    return CONFIG;
                default:
                    return null;
            }
        }

    }

    public enum KeywordType {
        STARTING_WORD, HELP_WORD, VARIABLE_TYPE_WORD, BLOCK_STARTER;
    }

    public static final HashMap<Character, Character> ENCLOSEMENTS = new HashMap<>(Map.ofEntries(Map.entry('(', ')'), Map.entry('{', '}'), Map.entry('[', ']'), Map.entry('"', '"')));
    public static final String[] OPERATORS = new String[]{"+", "-", "*", "/", "AND", "OR", "XOR", "==", "<", ">", "<=", ">="};

    public static Context readIn(File file) throws BSharpException {
        Context context = new Context();
        ArrayList<String> lines = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            lines = new ArrayList<>(br.lines().toList());
            lines.addFirst(null); //because first element will get cut off
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String line = lines.getFirst();
        try {
            readInCommandBlock(context, lines, null, null);
        } catch (LexerEndBlock e) {
            throw new CompileError("Reached the end while phrasing");
        }
        System.out.println(Arrays.toString(context.functionGet("main").getFunctionCommands()));
        return context;
    }

    public static Command readInSingleLine(Context context, String thisLine, ArrayList<String> followingLines) throws BSharpException, LexerEndBlock {
        if (thisLine.isEmpty()) return null;
        thisLine = Lexer.removeExcessBlanks(thisLine);
        if (thisLine.isEmpty()) return null;

        if (thisLine.charAt(0) == '/' && thisLine.charAt(1) == '/') return null; // is a comment

        //single Strings
        if (thisLine.charAt(0) == '"' && thisLine.charAt(thisLine.length() - 1) == '"')
            return new ConstantString(thisLine.substring(1, thisLine.length() - 1));

        //single Numbers
        if (isNumber(thisLine))
            return new ConstantNumber(Integer.parseInt(thisLine)); //if the whole line consists of only one number

        //unnecessary brackets on the outside
        if (encasementRemovable(thisLine, '('))
            return Lexer.readInSingleLine(context, thisLine.substring(1, thisLine.length() - 1), followingLines); //if there are brackets that can be removed


        //Operations
        for (String operator : OPERATORS) {
            int[] indexOfOperator = Lexer.indexOfNonEnclosedOperator(thisLine, operator);
            //if the operator is found in a non enclosed area
            if (indexOfOperator != null) {
                Command part1 = Lexer.readInSingleLine(context, thisLine.substring(0, indexOfOperator[0]), followingLines);
                Command part2 = Lexer.readInSingleLine(context, thisLine.substring(indexOfOperator[1] + 1), followingLines);
                if (!(part1 instanceof Getter))
                    throw new CompileError("\"" + thisLine.substring(0, indexOfOperator[0]) + "\" must return a value");
                if (!(part2 instanceof Getter))
                    throw new CompileError("\"" + thisLine.substring(indexOfOperator[1] + 1) + "\" must return a value");

                //if both are numbers do numerical operations
                if ((part1 instanceof NumberGetter || part1 instanceof Input) && (part2 instanceof NumberGetter || part2 instanceof Input) ) { //tf das geht in Java?
                    NumberGetter part1Getter = new AsNumberGetter(part1);
                    NumberGetter part2Getter = new AsNumberGetter(part2);

                    switch (operator) {
                        case "+":
                            return new Addition(part1Getter, part2Getter);
                        case "-":
                            return new Subtraction(part1Getter, part2Getter);
                        case "*":
                            return new Multiplication(part1Getter, part2Getter);
                        case "/":
                            return new Division(part1Getter, part2Getter);
                        case "AND":
                            return new ConnectOperator(part1Getter, part2Getter, ConnectOperator.Operator.AND);
                        case "OR":
                            return new ConnectOperator(part1Getter, part2Getter, ConnectOperator.Operator.OR);
                        case "XOR":
                            return new ConnectOperator(part1Getter, part2Getter, ConnectOperator.Operator.XOR);
                        case "==":
                            return new CompareOperator(part1Getter, part2Getter, CompareOperator.Operator.EQUALS);
                        case "<":
                            return new CompareOperator(part1Getter, part2Getter, CompareOperator.Operator.LESS);
                        case ">":
                            return new CompareOperator(part1Getter, part2Getter, CompareOperator.Operator.GREATER);
                        case "<=":
                            return new CompareOperator(part1Getter, part2Getter, CompareOperator.Operator.LESS_OR_EQUAL);
                        case ">=":
                            return new CompareOperator(part1Getter, part2Getter, CompareOperator.Operator.GREATER_OR_EQUAL);
                        default:
                            System.out.println("ddd");
                    }
                }

                switch (operator) {
                    case "+":
                        return new CombineText(part1, part2);
                    default:
                        throw new CompileError("Operator: \"" + operator + "\" cannot be used on String");
                }

            }
        }


        //Programming Commands
        String[] words = thisLine.split(" ");
        System.out.println(Arrays.toString(words));


        //SINGLE KEYWORD COMMANDS - Commands that only use a singe Keyword
        Keyword word1 = Keyword.getKeyword(words[0]);
        if (word1 == null) throw new CompileError(("\"" + words[0] + "\" is not syntax-known"));

        //Config
        if(word1 == Keyword.CONFIG) {
            Context.PrintStreamType typeOut;
            Context.PrintStreamType typeErr;
            Context.InputStreamType typeIn;
            typeOut = switch (words[1]) {
                case "console", "c" -> Context.PrintStreamType.SYSTEM_OUT;
                case "popup", "p" -> Context.PrintStreamType.JOPTIONPANE_OUT;
                default -> null;
            };
            typeErr = switch (words[2]) {
                case "console", "c" -> Context.PrintStreamType.SYSTEM_ERR;
                case "popup", "p" -> Context.PrintStreamType.JOPTIONPANE_ERR;
                default -> null;
            };
            typeIn = switch (words[3]) {
                case "console", "c" -> Context.InputStreamType.SYSTEM_IN;
                case "popup", "p" -> Context.InputStreamType.JOPTIONPANE_IN;
                default -> null;
            };
            try {
                context.config(typeOut, typeErr, typeIn);
            } catch (IllegalStateException e) {
                throw new CompileError("There can only be a maximum of 1 config Commands per File");
            }
            return null;
        }

        //Print
        if (word1 == Keyword.PRINT) {
            return new Print(context, (Getter) (Lexer.readInSingleLine(context, thisLine.substring(5), followingLines)));
        }

        //Input
        if (word1 == Keyword.INPUT) {
            return new Input(context, (Getter) (Lexer.readInSingleLine(context, thisLine.substring(5), followingLines)));
        }



        //TWO KEYWORD COMMANDS - Commands that use two Keywords
        Keyword word2 = Keyword.getKeyword(words[1]);
        if (word2 == null) throw new CompileError(("\"" + words[1] + "\" is not syntax-known"));

        //Int
        if (word1 == Keyword.INT) {
            final int firstTwoWordsLength = 7;
            if (word2 == Keyword.GET) {
                //if there is more than one word after the GET
                if (words.length != 3)
                    throw new CompileError(("\"" + words[2] + "\" is not a valid variable name"));
                return new IntGetCmd(context, words[2]);
            }
            if (word2 == Keyword.SET) {
                return new IntSetCmd(context, words[2], new AsNumberGetter(Lexer.readInSingleLine(context, thisLine.substring(firstTwoWordsLength + words[2].length() + 1), followingLines))); //the third thing is everything after the variable name
            }
            throw new CompileError(("\"" + word2 + "\" is not a valid argument at this point"));
        }

        //String
        if (word1 == Keyword.STRING) {
            final int firstTwoWordsLength = 10;
            if (word2 == Keyword.GET) {
                //if there is more than one word after the GET
                if (words.length != 3)
                    throw new CompileError(("\"" + words[2] + "\" is not a valid variable name"));
                return new StringGetCmd(context, words[2]);
            }
            if (word2 == Keyword.SET) {
                return new StringSetCmd(context, words[2], (StringGetter) Lexer.readInSingleLine(context, thisLine.substring(firstTwoWordsLength + words[2].length() + 1), followingLines)); //the third thing is everything after the variable name
            }
            throw new CompileError(("\"" + word2 + "\" is not a valid argument at this point"));
        }


        //Function
        if (word1 == Keyword.FUNCTION) {
            final int firstTwoWordsLength = 12;
            if (word2 == Keyword.START) {
                if (words.length != 3) throw new CompileError(("\"" + words[2] + "\" is not a valid function name"));
                ArrayList<Command> followingCommands = Lexer.readInCommandBlock(context, followingLines, Keyword.FUNCTION, words[2]);
                followingCommands.trimToSize();
                return new Function(context, words[2], followingCommands.toArray(new Command[followingCommands.size()]));
            } else if (word2 == Keyword.END) {
                if (words.length != 3) throw new CompileError(("\"" + words[2] + "\" is not a valid function name"));
                throw new LexerEndBlock(word1, words[2]);
            } else if (word2 == Keyword.STOP) {
                if (words.length != 3) throw new CompileError(("\"" + words[2] + "\" is not a valid function name"));
                return new CodeBlockStop(context, words[2]);
            } else if (word2 == Keyword.RUN) {
                if (words.length != 3) throw new CompileError(("\"" + words[2] + "\" is not a valid function name"));
                return new FunctionRunner(context, words[2]);
            }
            throw new CompileError(("\"" + word2 + "\" is not a valid argument at this point"));
        }

        if (word1 == Keyword.IF) {
            String backendIfName = backendFunctionName("If-Else", words[2]);
            if (word2 == Keyword.START) {
                Keyword word4 = Keyword.getKeyword(words[3]);
                //checks for "WHEN"-Keyword
                if (word4 != Keyword.WHEN)
                    throw new CompileError("If-Start needs to have a \"" + Keyword.WHEN.toString() + "\" keyword at Position 4");

                //Creates a function
                ArrayList<Command> followingCommands = Lexer.readInCommandBlock(context, followingLines, Keyword.IF, backendIfName);
                followingCommands.trimToSize();
                Function ifPart = new Function(context, backendIfName, followingCommands.toArray(new Command[followingCommands.size()]));

                //reads out a condition
                Command conditionAsCmd = readInSingleLine(context, combineElementsToString(words, 4), followingLines);
                if (!(conditionAsCmd instanceof Condition))
                    throw new CompileError("If-Condition needs to be a Condition");

                //creates whole block
                return new If(context, backendIfName, (Condition) conditionAsCmd, ifPart);
            } else if (word2 == Keyword.END) {
                if (words.length != 3) throw new CompileError(("\"" + words[2] + "\" is not a valid function name"));
                throw new LexerEndBlock(word1, backendIfName);
            } else if (word2 == Keyword.STOP) {
                if (words.length != 3) throw new CompileError(("\"" + words[2] + "\" is not a valid function name"));
                return new CodeBlockStop(context, backendIfName);
            }
            throw new CompileError(("\"" + word2 + "\" is not a valid argument at this point"));
        }

        if (word1 == Keyword.ELSE) {
            String backendIfName = backendFunctionName("If-Else", words[2]);
            if (word2 == Keyword.START) {

                //Creates a function
                ArrayList<Command> followingCommands = Lexer.readInCommandBlock(context, followingLines, Keyword.ELSE, backendIfName);
                followingCommands.trimToSize();
                Function ElsePart = new Function(context, backendIfName, followingCommands.toArray(new Command[followingCommands.size()]));

                //creates whole block
                return new Else(context, backendIfName, ElsePart);
            } else if (word2 == Keyword.END) {
                if (words.length != 3) throw new CompileError(("\"" + words[2] + "\" is not a valid function name"));
                throw new LexerEndBlock(word1, backendIfName);
            } else if (word2 == Keyword.STOP) {
                if (words.length != 3) throw new CompileError(("\"" + words[2] + "\" is not a valid function name"));
                return new CodeBlockStop(context, backendIfName);
            }
            throw new CompileError(("\"" + word2 + "\" is not a valid argument at this point"));
        }

        //THREE KEYWORD COMMANDS - Commands that use three Keywords
        Keyword word3 = Keyword.getKeyword(words[2]);
        if (word3 == null) throw new CompileError(("\"" + words[2] + "\" is not syntax-known"));


        return null;
    }

    public static ArrayList<Command> readInCommandBlock(Context context, ArrayList<String> lines, Keyword blockType, String blockName) throws BSharpException, LexerEndBlock {
        ArrayList<Command> commands = new ArrayList<>();

        while (lines.size() > 1) {
            lines.removeFirst();
            String line = lines.getFirst();
            try {
                Command lineAsCommand = Lexer.readInSingleLine(context, line, lines);
                if (lineAsCommand != null) commands.add(lineAsCommand);
            }
            //if a block should be stopped (e.g. "F Stop testFunction")
            catch (LexerEndBlock e) {
                //if it's this block
                if (e.getBlockType() == blockType && e.blockName.equals(blockName)) return commands;

                //if it's a block above
                lines.addFirst(line); //add back this line so that the top layer can read the
                return commands;
            }
//            System.out.println("commands" + Arrays.toString(commands.toArray()));
        }
        commands.trimToSize();
        return commands;
    }

    public static String backendFunctionName(String constructName, String functionName) {
        return "⮋//" + constructName + "//" + functionName;
    }


    public static boolean hasNoBlanks(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') return false;
        }
        return true;
    }


    /**
     * Returns the <b>beginning- and end-index</b> of the first instance of an operator (1 or 2 char long String) in a text where it's not enclosed by any enclosement method.
     * E.g. "(1*6)* 7" will return {5, 5} , "(1*6) == 7" will return {6, 7}
     *
     * @param text     the text to search in
     * @param operator the (1 or 2 chars long) String which will be searched
     * @return an int-Array with 2 values. The first one will be the beginning of the operator and the second one the end  |  null if no operator is found
     */
    public static int[] indexOfNonEnclosedOperator(String text, String operator) {
        if (operator.length() != 1 && operator.length() != 2 && operator.length() != 3)
            throw new IllegalArgumentException("Operator needs to be 1-3 characters");
        int openCases = 0;
        // wenn man die Anzahl der offenen Klammern immer größer als 1 ist (wie bei "((1+1) + 1)" und nicht bei "(1+1) + (1*2)"
        for (int i = 0; i + 1 < text.length(); i++) {
            if (ENCLOSEMENTS.containsKey(text.charAt(i))) openCases++;
            else if (ENCLOSEMENTS.containsValue(text.charAt(i))) openCases--;
            //if the operator is one char long
            if (operator.length() == 1) {
                if (text.charAt(i) == operator.charAt(0) && openCases == 0) return new int[]{i, i};
            }
            //if the operator is 2 chars long
            if (operator.length() == 2 && i >= 1) {
                if (text.charAt(i - 1) == operator.charAt(0) && text.charAt(i) == operator.charAt(1) && openCases == 0)
                    return new int[]{i - 1, i};
            }
            //if the operator is 2 chars long
            if (operator.length() == 3 && i >= 2) {
                if (text.substring(i-2, i+1).equals(operator) && openCases == 0)
                    return new int[]{i - 2, i};
            }
        }
        return null;
    }

    /**
     * If you can remove an encasement at the beginning and the end of an expression e.g."((1+1) + 1)"
     *
     * @param text           the expression
     * @param encasementOpen the encasement-opener '(', '{', '[' or '"'
     * @return if the outer encasement can be removed
     */
    public static boolean encasementRemovable(String text, char encasementOpen) {
        return Lexer.encasementRemovable(text, encasementOpen, false);
    }

    /**
     * If you can remove an encasement at the beginning and the end of an expression e.g."((1+1) + 1)"
     *
     * @param text                        the expression
     * @param encasementOpen              the encasement-opener '(', '{', '[' or '"'
     * @param allowOnlyOneEncasementLayer if the text is only allowed to be encased by one layer
     * @return if the outer encasement can be removed
     */
    public static boolean encasementRemovable(String text, char encasementOpen, boolean allowOnlyOneEncasementLayer) {
        char encasementClose = switch (encasementOpen) {
            case '(' -> ')';
            case '{' -> '}';
            case '[' -> ']';
            case '"' -> '"';
            default -> throw new RuntimeException("Unexpected encasement open: " + encasementOpen);
        };

        int openCases = 0;
        // wenn man die Anzahl der offenen Klammern immer größer als 1 ist (wie bei "((1+1) + 1)" und nicht bei "(1+1) + (1*2)"
        for (int i = 0; i + 1 < text.length(); i++) {
            if (text.charAt(i) == encasementOpen) openCases++;
            if (text.charAt(i) == encasementClose) openCases--;
            if (allowOnlyOneEncasementLayer && openCases != 1) return false;
            if (!allowOnlyOneEncasementLayer && openCases <= 0) return false;
        }
        return true;
    }


    public static String removeExcessBlanks(String input) {
        input = input.trim();
        if (input.isEmpty()) return "";
        StringBuilder output = new StringBuilder().append(input.charAt(0));
        for (int i = 1; i < input.length(); i++) {
            if (!(Character.isWhitespace(input.charAt(i)) && Character.isWhitespace(input.charAt(i - 1)))) {
                output.append(input.charAt(i));
            }
        }
        return output.toString();
    }

    public static boolean isNumber(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String[] removeFirst(String[] array) {
        String[] newArray = new String[array.length - 1];
        System.arraycopy(array, 1, newArray, 0, array.length - 1);
        return newArray;
    }

    public static String combineElementsToString(String[] input, int startIndex) {
        StringBuilder output = new StringBuilder();
        for (int i = startIndex; i < input.length; i++) {
            output.append(input[i]).append(" ");
        }
        return output.toString();
    }


//    public static String readLine(String line) {
//
//    }


}
