package collect.runtime.information.main;

import java.io.PrintWriter;

import org.apache.commons.cli.*;

public class Arguments {
    private Options ops;
    private CommandLine commandLine;
    
    public Arguments(){}
    
    public void createOptions(){
        this.ops = new Options();
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
                            .required()
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
                            .required()
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
                            .desc("Print this help message and exit program")
                            .hasArg(false)
                            .longOpt("help")
                            .build());
        // help command
        ops.addOption(Option.builder("ho")
                            .argName("Option")
                            .desc("Print help message for one option and exit program")
                            .hasArg()
                            .longOpt("helpOption")
                            .build());
    }
    public void parseCommandLine(String[] args){
        CommandLineParser parser = new DefaultParser();
        try {
            this.commandLine = parser.parse(ops, args);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        if (commandLine.hasOption("h")) {
            this.help();
            System.exit(0);
        }
        if (commandLine.hasOption("ho")){
            String op = commandLine.getOptionValue("ho");
            helpOption(op);
            System.exit(0);
        }
    }
    public void help(){
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("CURE", this.ops);
    }
    private static void helpOption(String op){
        String msg = "Help message for -"+op;
        switch (op){
            case "args":{
                msg+= "\t Arguments for sub vm"
                        + "\t Seperated by white space"
                        + "\t : e.g.: -args arg1 arg2...";
                break;
            }
            case "d":{
                msg+="\t Field path depth that CURE will get into"
                        + "\t An integer number; default is 1"
                        + "\t : 0, iterate through all field path"
                        + "\t : forced to be 1 if -level is 1";
                break;
            }
            case "i":{
                msg+="\t Input file path"
                        + "\t A file path; read stop points and corresponding conditions from this file";
                break;
            }
            case "l":{
                msg+="\t Information complexity level that CURE will record"
                        + "\t An integer number; default is 1"
                        + "\t 1, records methods' name; -depth is forced to be 1"
                        + "\t 2, includes 1"
                        + "\t : records primitive field's value"
                        + "\t : records NULL/NOT_NULL for reference-type fields"
                        + "\t : records object number of object classes"
                        + "\t : records NULL/NOT_NULL of array/list/map/set fields"
                        + "\t : records element number of array/list/map/set fields"
                        + "\t : until reaches the depth"
                        + "\t 3, includes 2"
                        + "\t : iterates into reference-type fields"
                        + "\t : iterates into each element of array/list/map/set"
                        + "\t : until reaches the depth";
                break;
            }
            case "m":{
                msg+="\t Main class that will run in sub virtual machine, full class name";
                break;
            }
            case "o":{
                msg+="\t Output file path"
                        + "\t A file path; write stop points and corresponding conditions to this file";
                break;
            }
            case "oc":{
                msg += "\t Object classes"
                        + "\t Full class names, whose object will be monitore while the sub vm is running"
                        + "\t usually are changed classes of an update"
                        + "\t the classes where stop points are located in will also be treated as object classes"
                        + "\t separated by white space"
                        + "\t : e.g. package1.class1 package2.class2 ...";
                break;
            }
            case "sp":{
                msg += "\t Stop points, where sub vm will stop at during runtime"
                        + "\t sperarated by white space"
                        + "\t the classes where stop points are located in are treated as object classes"
                        + "\t a point can be defined as: "
                        + "\t : package.class-method-descriptor:enter@AT@2@10"
                        + "\t : package.class-method-descriptor:exit@AFTER@3"
                        + "\t : package.class:lineno@BEFORE@10"
                        + "\t package.class-method-descriptor:enter/exit will stop at the entry/exit of the method"
                        + "\t package.class:lineno will stop at the line number (source code line) of the class"
                        + "\t @AT@2@10 only stop at a point while the 2nd and 10th times passing this point"
                        + "\t : non-zero; these number should be in ascending order"
                        + "\t @AFTER@3 only stop at a point AFTER the 3rd (included) time passing this point, won't stop at 1st and 2nd times"
                        + "\t : non-zero;"
                        + "\t @BEFORE@4 only stop at a point BEFORE the 4th (excluded) time passing this point, won't stop at 4th, 5th etc times"
                        + "\t : non-zero; bigger than 1"
                        + "\t @AFTER@1 will stop at all times passing a point";
                break;
            }
            case "vmcp":{
                msg += "\t Class path that are used in sub vm";
                break;
            }
            default:{
                msg+="\t Unrecognized option "+ op;
            }
        }
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);
        formatter.printWrapped(pw, formatter.getWidth(),1, msg);
        pw.flush();
    }
    public VMArgs getVmArgs(){
        VMArgs vmargs = new VMArgs();
        vmargs.setClassPath(this.commandLine.getOptionValue("vmcp", ""));
        vmargs.setMainClass(this.commandLine.getOptionValue("m", ""));
        if(this.commandLine.hasOption("args"))
            for(String str: this.commandLine.getOptionValues("args"))
                vmargs.addArgv(str);
        return vmargs;
    }
    public VMCmds getVmCmds(){
        VMCmds vmcmds = new VMCmds();
        if(this.commandLine.hasOption("sp"))
            for(String str: this.commandLine.getOptionValues("sp")){
                ProgramPoint pp = new ProgramPoint(str);
                    vmcmds.addCmd(pp);
            }
        return vmcmds;
    }
    public VMInfo getVmInfo(){
        VMInfo vminfo = new VMInfo();
        if(this.commandLine.hasOption("oc"))
            for(String str: this.commandLine.getOptionValues("oc"))
                vminfo.addObjClass(str);
        if(this.commandLine.hasOption("sp"))
            for(String str: this.commandLine.getOptionValues("sp")){
                if(str.contains("-"))
                    vminfo.addObjClass(str.substring(0, str.indexOf("-")));
                else
                    vminfo.addObjClass(str.substring(0,str.indexOf(":")));
            }
        if(this.commandLine.hasOption("o"))
            vminfo.setOutputFile(this.commandLine.getOptionValue("o"));
        if(this.commandLine.hasOption("i"))
            vminfo.setInputFile(this.commandLine.getOptionValue("i"));
        String level = this.commandLine.getOptionValue("l", "1");
        String depth = this.commandLine.getOptionValue("d", "1");
        vminfo.setLevelDeepth(Integer.valueOf(level), Integer.valueOf(depth));
        return vminfo;
    }
}
