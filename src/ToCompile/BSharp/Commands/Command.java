package ToCompile.BSharp.Commands;

import ToCompile.BSharp.*;
import ToCompile.BSharp.Exceptions.BSharpRuntimeException;

import java.io.Serializable;

/**
 * The highest class of the Project
 * All Commands are children of this Class
 * They need to have a run()-method and a toString()-method
 */
public abstract class Command implements Serializable {
    static final long serialVersionUID = 1L;
    Context context;
    public Command(Context context){
        this.context = context;
    }

    /**
     * The command executes itself
     * @return ether null or the result of this command (if it is a Getter)
     */
    public abstract Object run() throws BSharpRuntimeException;

    /**
     * A String representation of this command
     * @return a String representation of this command
     */
    @Override
    public abstract String toString();

}
