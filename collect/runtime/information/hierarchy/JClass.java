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

    /** object name and ObjectReference, may not be needed */
    private HashMap<Long, JObjectValue> instances = new HashMap<Long, JObjectValue>();

    /**
     * @param type
     * @param fieldName, while creating a new object from scratch, there isn't fieldName, should use "" or null.
     * @param eventThread
     */
    public JClass(ReferenceType type, String fieldName, ThreadReference eventThread) {
        super(type, type.name(),fieldName);
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

    /**
     * for input creation.
     * @param className
     */
    public JClass(String className){
        super(null, className, null);
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
