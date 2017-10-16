package collect.runtime.information.hierarchy;

import java.util.HashMap;

import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.Field;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Type;

import collect.runtime.information.value.JObjectValue;

public class JClass extends Base {
    /** field name and jfield */
    private HashMap<String, JField> fields = new HashMap<String, JField>();
    /** method name and jmethods */
    @Deprecated
    private HashMap<String, JMethod> methods = new HashMap<String, JMethod>();
    /** constructor name and constructors (use jmethod) */
    @Deprecated
    private HashMap<String, JMethod> constructors = new HashMap<String, JMethod>();

    /** object name and ObjectReference, may not be needed */
    private HashMap<String, JObjectValue> instances = new HashMap<String, JObjectValue>();

    public JClass(Type type, String typename, String name) {
        super(type, typename, name);
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     * @param type,
     *            cannot be null
     * @param name
     */
    public JClass(Type type, String name) {
        this(type, type.name(), name);
    }

    public JClass(ReferenceType type, String name, ThreadReference eventThread) {
        this(type, type.name(), name);
        // get all fields of this class
        try {
            for (Field field : type.visibleFields())
                this.fields.put(field.name(), new JField(field.type(), field.typeName(), field.name(), field));
        } catch (ClassNotLoadedException e) {
            e.printStackTrace();
        }
        // get all instances of this class
        for (ObjectReference objRefer : type.instances(0)) {
            String id = String.valueOf(objRefer.uniqueID());
            JObjectValue jov = new JObjectValue(objRefer, name, eventThread, null);
            jov.createObject();
            this.instances.put(id, jov);
        }
    }

    public void printAllInstances() {
        for (JObjectValue jov : this.instances.values())
            jov.printObject();
    }
    public int getInstanceNumber(){
        return this.instances.size();
    }

    public JClass clone() {
        return this;
    }
}
