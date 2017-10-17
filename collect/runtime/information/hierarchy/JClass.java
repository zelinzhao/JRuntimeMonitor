package collect.runtime.information.hierarchy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.Field;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Type;

import collect.runtime.information.condition.Condition;
import collect.runtime.information.main.VMInfo;
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
    private HashMap<Long, JObjectValue> instances = new HashMap<Long, JObjectValue>();

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

    /**
     * 
     * @param type
     * @param fieldName, while creating a new object from scratch, there isn't fieldName, should use "" or null.
     * @param eventThread
     */
    public JClass(ReferenceType type, String fieldName, ThreadReference eventThread) {
        this(type, type.name(), fieldName);
        // get all fields of this class
        try {
            for (Field field : type.visibleFields())
                this.fields.put(field.name(), new JField(field.type(), field.typeName(), field.name(), field));
        } catch (ClassNotLoadedException e) {
            e.printStackTrace();
        }
        // get all instances of this class
        for (ObjectReference objRefer : type.instances(0)) {
            JObjectValue jov = new JObjectValue(objRefer, fieldName, eventThread, null);
            jov.createObject();
            this.instances.put(objRefer.uniqueID(), jov);
        }
    }

//    public void extractObjsConditions() {
//        for (JObjectValue jov : this.instances.values()){
//            jov.extractConditions();
//        }
//    }
    public int getInstanceNumber(){
        return this.instances.size();
    }

    public JClass clone() {
        return this;
    }
}
