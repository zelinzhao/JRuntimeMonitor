package collect.runtime.information.main;

import java.io.File;
import java.io.IOException;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.VMStartException;
import org.apache.commons.cli.*;

/**
 * @author Zelin Zhao。 CRI is the main class, stands for Collect Runtime
 *         Information. CRI will stop the running Java program at specific
 *         program point and collect the following information: 1) methods'
 *         names in the stack; 2) the state (value) of objects that belong to
 *         changed classes and the class that the specific program point locate
 *         in.
 */
public class CRI {

    private static void usageError(String message) {
        System.out.println(message);
        System.exit(0);
    }

    private static boolean isValidArguments(String arguments) {
        if (arguments.startsWith("-"))
            return false;
        return true;
    }

    public static void main(String[] argv) throws IOException, IllegalConnectorArgumentsException, VMStartException {
        Arguments argument = new Arguments();
        argument.createOptions();
        
        if(argv.length==0){
            argument.help();
            usageError("You must give some arguments");
        }
        argument.parseCommandLine(argv);
        VMConnection vm = new VMConnection();
        vm.setVmArgs(argument.getVmArgs());
        vm.setVmCmds(argument.getVmCmds());
        vm.setVmInfo(argument.getVmInfo());
        vm.start();
    }
}