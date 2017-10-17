package collect.runtime.information.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.VMStartException;

import collect.runtime.information.value.JNullValue;
import collect.runtime.information.value.JObjectValue;
import collect.runtime.information.value.JValue;

/**
 * @author Zelin Zhao。 CRI is the main class, stands for Collect Runtime
 *         Information. CRI will stop the running Java program at specific
 *         program point and collect the following information: 1) methods'
 *         names in the stack; 2) the state (value) of objects that belong to
 *         changed classes and the class that the specific program point locate
 *         in.
 */
public class CRI {

    private static void usage() {
        System.out.println("usage for Collect Runtime Information:\n"
                + "-objclass class1,class2 -vmcp classpath -main mainclass -args arg1 arg2 -stopat point1,point2 -level 1 -depth 1\n"
                + "In detail:\n"
                + "     -objclass package.class1,package.class2 (CRI traces objects of these classes)\n"
                + "         (Usually they are changed classes. May be not provided.)\n"
                + "     -vmcp classpath (directories separated by " + File.pathSeparator + ")\n"
                + "         (use -vmcp to distinguish from -cp)\n" + "     -main mainclass (must be provided)\n"
                + "     -args arg1 arg2 (arguments which are used, may be not provided)\n"
                + "     -stopat package.class-method-descriptor:enter,package.class-method-descriptor:exit,"
                + "package.class:lineno\n"
                + "         (for constructors, use \"<init>\"; for static initializers, use \"<clinit>\")\n"
                + "         (\"package.class-method-descriptor:enter\" means stop at the entrance of this method)\n"
                + "         (\"package.class-method-descriptor:exit\" means stop at the exit of this method)\n"
                + "         (\"package.class:lineno\" means stop at the line of this class and lineno must be an integer)\n"
                + "         (CRI also traces objects of these classes)\n"
                + " -help to print this information.\n"
                + " -level \n"
                + "         1 (record methods' name at stop point. depth is 1.)\n"
                + "         2 (includes 1; record primitive-type fields' value,\n"
                + "             record null/not-null for reference-type fields,\n"
                + "             record size or length for list, map, set or array.\n"
                + "         3 (includes 1; record primitive-type fields' value,\n"
                + "             iterate into reference-type fields, record primitive-type fields' value,"
                + "             record each element of set, array, map or list,\n"
                + "             stop at depth and record null/not-null for reference-type fields of last fields.)\n"
                + "         default is 1\n"
                + " -depth \n"
                + "         -1 (iterate through fields, no matter the depth is.)\n"
                + "         other number\n"
                + "         default is 1\n"
                + "         while -level is 1, depth is 1.");
    }

    private static void usageError(String message) {
        System.out.println(message);
        usage();
        System.exit(0);
    }

    private static boolean isValidArguments(String arguments) {
        if (arguments.startsWith("-"))
            return false;
        return true;
    }

    public static void main(String[] argv) throws IOException, IllegalConnectorArgumentsException, VMStartException {
        if(argv.length==0)
            usageError("You must give some arguments");
        int level = 1;
        int depth = 1;
        VMArgs vmargs = new VMArgs();
        VMCmds vmcmds = new VMCmds();
        VMInfo vminfo = new VMInfo();

        boolean inArgvs = false;
        
        for (int i = 0; i < argv.length; i++) {
            String token = argv[i];
            if (token.equals("-objclass")) {
                String classes = argv[++i];
                if (!isValidArguments(classes))
                    usageError("You should provide some classes after -objclass, or else not use -objclass.");
                String[] splitClasses = classes.split(",");
                for (String str : splitClasses)
                    vminfo.addObjClass(str);
            } else if (token.equals("-vmcp")) {
                String vmCp = argv[++i];
                if (!isValidArguments(vmCp))
                    usageError("You should provide classpath after -vmcp");
                else
                    vmargs.setClassPath(vmCp);
            } else if (token.equals("-main")) {
                String mainClass = argv[++i];
                if (!isValidArguments(mainClass))
                    usageError("You shold provide main class after -main");
                else
                    vmargs.setMainClass(mainClass);
            } else if (token.equals("-args")) {
                inArgvs = true;
            } else if (token.equals("-stopat")) {
                String points = argv[++i];
                for (String str : points.split(",")) {
                    if (str.contains("-")) {
                        MethodPoint mp = new MethodPoint(str);
                        vmcmds.addCmd(mp);
                        vminfo.addObjClass(mp.getFullClassName());
                    } else {
                        LinePoint lp = new LinePoint(str);
                        vmcmds.addCmd(lp);
                        vminfo.addObjClass(lp.getFullClassName());
                    }
                }
            } else if (inArgvs) {
                String arg = argv[i];
                if (!isValidArguments(arg)) {
                    inArgvs = false;
                    i--;
                } else
                    vmargs.addArgv(arg);
            } else if(token.equals("-level")){
                level = Integer.valueOf(argv[++i]);
            } else if(token.equals("-depth")){
                depth = Integer.valueOf(argv[++i]);
            } else if(token.equals("\\")){
            } else {
                usageError("Something is wrong. Please see the usage information.");
            }
        }
        vminfo.setLevelDeepth(level, depth);
        
        VMConnection vm = new VMConnection();
        vm.setVmArgs(vmargs);
        vm.setVmCmds(vmcmds);
        vm.setVmInfo(vminfo);
        vm.start();
    }
}