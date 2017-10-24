
import java.io.PrintWriter;

import org.apache.commons.cli.*;

public class test {

    public static void main(String[] args) throws ParseException {

        Options ops = new Options();
        // Object classes
        ops.addOption(Option.builder("oc")
                            .argName("ObjectClass")
                            .desc("Classes whose objects will be monitored")
                            .hasArgs()
                            .longOpt("objClass")
                            .build());
        // Sub-vm classpath
        ops.addOption(Option.builder("vmcp")
                            .argName("ClassPath")
                            .desc("Class path that is used in sub vm")
                            .hasArg()
                            .build());
        // Sub-vm main class
        ops.addOption(Option.builder("m")
                            .argName("MainClass")
                            .desc("Main class that runs in sub vm")
                            .hasArg()
                            .longOpt("main")
                            .build());
        // Sub-vm arguments
        ops.addOption(Option.builder("args")
                            .argName("Arguments")
                            .desc("Arguments that are used in sub vm")
                            .hasArgs()
                            .build());
        //Stop points
        ops.addOption(Option.builder("sp")
                            .argName("StopPoints")
                            .desc("Stop points that sub vm will stop at")
                            .hasArgs()
                            .longOpt("stopPoints")
                            .build());
        // Information level
        ops.addOption(Option.builder("l")
                            .argName("InformationLevel")
                            .desc("Information complexity that CURE records")
                            .hasArg()
                            .longOpt("level")
                            .build());
        // Depth
        ops.addOption(Option.builder("d")
                            .argName("ObjectDepth")
                            .desc("Object depth that CURE will get into")
                            .hasArg()
                            .longOpt("depth")
                            .build());
        // Output file
        ops.addOption(Option.builder("o")
                            .argName("FILE")
                            .desc("/Write/to/this/file/path")
                            .hasArg()
                            .longOpt("output")
                            .build());
        // Input file
        ops.addOption(Option.builder("i")
                            .argName("FILE")
                            .desc("/Read/from/this/file/path")
                            .hasArg()
                            .longOpt("input")
                            .build());
        
        // help
        ops.addOption(Option.builder("h")
                            .desc("Print this help message")
                            .hasArg(false)
                            .longOpt("help")
                            .build());
        // help command
        ops.addOption(Option.builder("ho")
                            .argName("Option")
                            .desc("Print help message for one option")
                            .hasArg()
                            .longOpt("helpOption")
                            .build());
        

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(ops, args);

        if (cmd.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("CURE", ops);
            PrintWriter pw = new PrintWriter(System.out);
            String msg = "Specified help message" + "\taaa";
            formatter.printWrapped(pw, formatter.getWidth(),1, msg);
            pw.flush();
        }
    }
}