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
import collect.runtime.information.condition.MethodExistCondition;
import collect.runtime.information.condition.ObjectNumberCondition;
import collect.runtime.information.hierarchy.JClass;

enum Level {
    /** only active methods' name. depth always 1. */
    ONE,
    /**
     * active methods' name and primitive-type fields' value.
     * size of array, list, set or map.
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
    
    /** for output conditions at stop points*/
    private List<Condition> outputConditions = new ArrayList<Condition> ();
    /** for input conditions. point id and conditions*/
    private HashMap<String, List<Condition>> inputConditions = new HashMap<String, List<Condition>>();

    /** default is 1 */
    private Level level = Level.ONE;
    /** default is 1. 0 is no limit. */
    private int depth = 1;

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
            this.level = Level.ONE;
            this.depth = 1;
        } else if (level == 2) {
            this.level = Level.TWO;
            this.depth = deepth;
        } else if (level == 3) {
            this.level = Level.THREE;
            this.depth = deepth;
        }
    }

    public void clearInfo() {
        this.outputConditions.clear();
        for (String objCla : this.objClasses)
            this.targetJClasses.put(objCla, null);
    }

    public void addJClass(String name, JClass jclass) {
        this.targetJClasses.put(name, jclass);
    }

    public void extractInfoFromVm(VirtualMachine vm, ThreadReference eventThread) {
        // get methods' name. level is one, two or three.
        try {
            for (StackFrame frame : eventThread.frames()) {
                Method met = frame.location().method();
                this.outputConditions.add(new MethodExistCondition(met));
            }
        } catch (IncompatibleThreadStateException e) {
            e.printStackTrace();
        }
        // get instances of targeted classes, level is two or three
        if (this.level == Level.TWO || this.level == Level.THREE)
            for (String className : this.objClasses) {
                List<ReferenceType> referTypes = vm.classesByName(className);
                if (!referTypes.isEmpty()) {
                    ReferenceType oneType = referTypes.get(0);
                    JClass jclass = new JClass(oneType, className, eventThread);
                    this.outputConditions.add(new ObjectNumberCondition(jclass));
                    this.targetJClasses.put(className, jclass);
                }
            }
    }
    public void printInfo(){
        
    }
    
    public void recordConditions(){
        
    }
    private void printMessage(String message) {
        System.out.println("[vm] message: " + message);
    }
}
