package collect.runtime.information.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.ClassType;
import com.sun.jdi.Field;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.InvalidTypeException;
import com.sun.jdi.InvocationException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Location;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventIterator;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.ModificationWatchpointEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.event.VMStartEvent;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.MethodEntryRequest;
import com.sun.jdi.request.ModificationWatchpointRequest;

import collect.runtime.information.hierarchy.JClass;
import collect.runtime.information.value.JNullValue;
import collect.runtime.information.value.JObjectValue;
import collect.runtime.information.value.JValue;

public class VMConnection extends VM {

    private VMArgs args;
    private VMCmds cmds;
    private VMInfo info;
    private VMOutput output;
    private VMOutput errorout;

    private Process process;
    private EventRequestManager eventRequestManager;

    protected ThreadReference eventthread; // for current event
    

    public void setVmArgs(VMArgs args) {
        this.args = args;
    }


    public VMArgs getVmArgs() {
        return this.args;
    }

    public void setVmCmds(VMCmds cmds) {
        this.cmds = cmds;
    }

    public VMCmds getVmCmds() {
        return this.cmds;
    }
    
    public void setVmInfo(VMInfo info){
        this.info = info;
    }

    public void showArgsCmds() {
        printMessage(args.toString());
        printMessage(cmds.toString());
    }
    
    public boolean isConnected(){
        return this.connected;
    }
    
    private LaunchingConnector findLaunchingConnector() {
        List connectors = Bootstrap.virtualMachineManager().allConnectors();
        Iterator iter = connectors.iterator();
        while (iter.hasNext()) {
            Connector connector = (Connector) iter.next();
            if ("com.sun.jdi.CommandLineLaunch".equals(connector.name())) {
                return (LaunchingConnector) connector;
            }
        }
        printMessage("Cannot find launching connector for sub vm");
        System.exit(-1);
        return null;
    }

    private Map getConnectorArguments(LaunchingConnector connector) {
        Map arguments = connector.defaultArguments();
        Connector.Argument mainArg = (Connector.Argument) arguments.get("main");
        Connector.Argument suspendArg = (Connector.Argument) arguments.get("suspend");
        Connector.Argument optionsArg = (Connector.Argument) arguments.get("options");
        String mainvalue = args.getMainClass() + " " + args.getArgvs();
        String optionsValue = args.getClassPathOption();
        mainArg.setValue(mainvalue);
        suspendArg.setValue("true");
        optionsArg.setValue(optionsValue);
        return arguments;
    }

    public void start() throws IOException, IllegalConnectorArgumentsException, VMStartException {
        LaunchingConnector connector = findLaunchingConnector();
        Map arguments = getConnectorArguments(connector);
        vm = connector.launch(arguments);
        process = vm.process();
        
        connected = true;

        this.eventRequestManager = vm.eventRequestManager();
        stopClassPrepareRequest();
        
        this.output = new VMOutput("vm_output_thread", process.getInputStream());
        this.output.start();
        this.errorout = new VMOutput("vm_error_ourput_thread", process.getErrorStream());
        this.errorout.start();
        eventLoop();
    }

    public void eventLoop(){
        EventQueue queue = vm.eventQueue();
        while (connected) {
            try {
                EventSet eventSet = queue.remove();
                EventIterator iter = eventSet.eventIterator();
                while (iter.hasNext()) {
                    handleEvent(iter.nextEvent());
                    eventSet.resume();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ClassNotLoadedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (AbsentInformationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IncompatibleThreadStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvalidTypeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    

    private void handleEvent(Event event) throws ClassNotLoadedException, AbsentInformationException,
            IncompatibleThreadStateException, InvalidTypeException, InvocationException {
        if (event instanceof VMStartEvent) {
            printMessage("VM started");
        } else if (event instanceof ClassPrepareEvent) {
            ClassPrepareEvent classevent = (ClassPrepareEvent) event;
            ReferenceType classref = classevent.referenceType();
            String classname = classref.name();
            // stop class
            if (cmds.hasStopClass(classname)) {
                stopClassEvent(classref);
            }
        } else if (event instanceof BreakpointEvent) {
            this.info.clearInfo();
            
            BreakpointEvent breakevent = (BreakpointEvent) event;
            ProgramPoint pp = this.cmds.getStopCmdsByEvent(breakevent);
            if(pp==null){
                printMessage("stop a an unrecognized point");
                return;
            }
            // @@,package.class, method, descriptor
            this.eventthread = breakevent.thread();
            this.info.createObjectsAndConditions(vm, eventthread);
            try {
                this.info.printConditions(pp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            //print all targeted objects here
        } else if ((event instanceof VMDisconnectEvent) || (event instanceof VMDeathEvent)) {
            connected = false;
            this.output.connected = false;
            this.errorout.connected = false;
        }
    }

    private void stopClassEvent(ReferenceType classref) throws ClassNotLoadedException, AbsentInformationException {
        for (ProgramPoint cmd : cmds.getStopCmdsByClass(classref.name())) {
            if(cmd.isOther()){
                printMessage("the following stop point may be incorrect");
                printMessage(cmd.toString());
            } else if (cmd.isLinePoint()) {
                List<Location> location = classref.locationsOfLine(((LinePoint)cmd).getLineNo());
                if(location!=null && location.size()>0)
                    createBreakPointRequest(location.get(0));
            } else {
                ClassType classtype = (ClassType) classref;
                MethodPoint mp = (MethodPoint)cmd;
                if (mp.getMethodDesc() == null) {
                    printMessage("need method signature for method: " + mp.getMethodName());
                    return;
                }
                Method method = classtype.concreteMethodByName(mp.getMethodName(), mp.getMethodDesc());
                if (method == null) {
                    printMessage("no method " + mp.getMethodName() + " " + mp.getMethodDesc() + " in " + classref.name());
                    return;
                } else {
                    List<Location> locations = method.allLineLocations();
                    if (mp.isMethodEnter()) {
                        createBreakPointRequest(locations.get(0));
                        mp.setLineNo(locations.get(0).lineNumber());
                    } else if (mp.isMethodExit()) {
                        createBreakPointRequest(locations.get(locations.size() - 1));
                        mp.setLineNo(locations.get(locations.size()-1).lineNumber());
                    }
                }
            }
        }
    }

    private void stopClassPrepareRequest() {
        Set<String> stopClass = cmds.getStopClass();
        if (stopClass.isEmpty()) {
            printMessage("has no stop classes.");
        }
        Iterator iter = stopClass.iterator();
        while (iter.hasNext()) {
            String classname = (String) iter.next();
            printMessage("will stop in " + classname);
            createClassPrepareRequest(classname);
        }
    }

    private void createClassPrepareRequest(String classname) {
        ClassPrepareRequest classPrepareRequest = eventRequestManager.createClassPrepareRequest();
        classPrepareRequest.addClassFilter(classname);
        classPrepareRequest.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
        classPrepareRequest.enable();
    }

    private void createBreakPointRequest(Location location) {
        BreakpointRequest breakrequest = this.eventRequestManager.createBreakpointRequest(location);
        breakrequest.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
        breakrequest.enable();
    }

    private void printMessage(String message) {
        System.out.println("[vm] message: " + message);
    }
}
