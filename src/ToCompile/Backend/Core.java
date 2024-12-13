package ToCompile.Backend;

import ToCompile.BSharp.Context;
import ToCompile.BSharp.Exceptions.BSharpException;

import java.io.*;

public class Core {


    public static File compileCodetoClassFile(String outputFilePathPlusName, Context context, String defaultFunctionName) throws BSharpException {
        if (!context.hasFunction(defaultFunctionName))
            throw new BSharpException("No function named" + defaultFunctionName + " found!");
        File out = new File(outputFilePathPlusName);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(out));
            oos.writeObject(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    // Method to load an object from a file
    public static Context readCodeFromFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (Context) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to load an object from a file
    public static Context readCodeFromStream(ObjectInputStream ois) {
        try {
            return (Context) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
