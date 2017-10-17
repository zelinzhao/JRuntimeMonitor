package collect.runtime.information.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;

import collect.runtime.information.condition.Condition;
import collect.runtime.information.condition.ElementNumberCondition;
import collect.runtime.information.condition.FieldValueCondition;
import collect.runtime.information.condition.MethodExistCondition;
import collect.runtime.information.condition.ObjectNumberCondition;
import collect.runtime.information.hierarchy.JClass;

enum Level {
    /** only active methods' name. depth always 1. */
    ONE,
    /**
     * active methods' name and primitive-type fields' value. size of array,
     * list, set or map. object number.
     */
    TWO,
    /**
     * active methods' name, primitive-type fields' value, iterate through
     * reference-type fields until depth is meet.
     */
    THREE
}

public class VMInfo {
    private HashSet<String> objClasses = new HashSet<String>();
    private HashMap<String, JClass> targetJClasses = new HashMap<String, JClass>();

    // static fields
    /** for output conditions at stop points */
    public static List<Condition> outputConditions = new ArrayList<Condition>();
    /** for input conditions. point id and conditions */
    public static HashMap<String, List<Condition>> inputConditions = new HashMap<String, List<Condition>>();
    /** default is 1 */
    public static Level LEVEL = Level.ONE;
    /** default is 1. 0 is no limit. */
    public static int DEPTH = 1;

    public void addObjClass(String objClass) {
        this.objClasses.add(objClass);
    }

    public void setObjClasses(HashSet<String> classes) {
        this.objClasses = classes;
    }

    public HashSet<String> getObjClasses() {
        return this.objClasses;
    }

    public boolean hasObjClass(String classname) {
        return this.objClasses.contains(classname);
    }

    public void setLevelDeepth(int level, int deepth) {
        if (level == 1) {
            LEVEL = Level.ONE;
            DEPTH = 1;
        } else if (level == 2) {
            LEVEL = Level.TWO;
            DEPTH = deepth;
        } else if (level == 3) {
            LEVEL = Level.THREE;
            DEPTH = deepth;
        }
    }

    /**
     * If LEVEL is THREE, this tool will go into reference object and element in container classes.
     * If LEVEL is TWO, this tool will get NULL/NOT_NULL of reference object and NULL/element_number of container classes.
     * If LEVEL is ONE, this tool only monitor methods on stacks.
     * @return
     */
    public static boolean intoReferenceObjectAndContainerElement(){
        if(LEVEL == Level.THREE)
            return true;
        return false;
    }
    public void clearInfo() {
        outputConditions.clear();
        for (String objCla : this.objClasses)
            this.targetJClasses.put(objCla, null);
    }

    public void addJClass(String name, JClass jclass) {
        this.targetJClasses.put(name, jclass);
    }

    public void createObjectsAndConditions(VirtualMachine vm, ThreadReference eventThread) {
        // get methods' name. level is one, two or three.
        try {
            for (StackFrame frame : eventThread.frames()) {
                Method met = frame.location().method();
                outputConditions.add(new MethodExistCondition(met));
            }
        } catch (IncompatibleThreadStateException e) {
            e.printStackTrace();
        }
        // get instances of targeted classes, level is two or three
        if (LEVEL == Level.TWO || LEVEL == Level.THREE){
            //here is first step of iterating through objects and fields.
            for (String className : this.objClasses) {
                List<ReferenceType> referTypes = vm.classesByName(className);
                if (!referTypes.isEmpty()) {
                    ReferenceType oneType = referTypes.get(0);
                    // Conditions are created in JCreateVisitorImplement while
                    // creating objects
                    // here, fieldname is null, using "" or null.
                    JClass jclass = new JClass(oneType, null, eventThread);
                    outputConditions.add(new ObjectNumberCondition(jclass));
                    this.targetJClasses.put(className, jclass);
                }
            }
            
        }
    }

    public void printConditions() {
        // for(Condition con: outputConditions){
        for (int i = outputConditions.size()-1; i >= 0; i--) {
            Condition con = outputConditions.get(i);
            if (con instanceof ElementNumberCondition) {
                printMessage(((ElementNumberCondition) con).toString());
            } else if (con instanceof FieldValueCondition) {
                printMessage(((FieldValueCondition) con).toString());
            } else if (con instanceof MethodExistCondition) {
                printMessage(((MethodExistCondition) con).toString());
            } else if (con instanceof ObjectNumberCondition) {
                printMessage(((ObjectNumberCondition) con).toString());
            } else {
                printMessage("unrecoginzed condition");
            }
        }
    }

    // public void recordConditions(){
    //
    // }
    private void printMessage(String message) {
        System.out.println("[vm] message: " + message);
    }
}
