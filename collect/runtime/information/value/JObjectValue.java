package collect.runtime.information.value;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.Field;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.PrimitiveType;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Type;
import com.sun.jdi.Value;
import com.sun.jdi.VoidValue;

import collect.runtime.information.condition.Condition;
import collect.runtime.information.condition.FieldValueCondition;
import collect.runtime.information.hierarchy.JField;
import collect.runtime.information.main.VMInfo;

public class JObjectValue extends JValue {
    private ObjectReference object;
    private ReferenceType type;
    private HashMap<String, JValue> fieldValues = new HashMap<String, JValue>();
    private HashMap<String, JValue> staticFieldValues = new HashMap<String, JValue>();

    /**
     * Be careful with this constructor.
     * 
     * @param value
     * @param name
     * @param eventthread
     * @param field
     *            field is null if this object is a top object
     */
    public JObjectValue(ObjectReference value, String name, ThreadReference eventthread, Field field) {
        super(name, eventthread, field);
        this.object = value;
        this.type = object.referenceType();
        if (field == null) {
            this.topLevelObjId = value.uniqueID();
        }
        this.realAsString = JValue.NOT_NULL;
    }
    
    public JObjectValue(String real){
        this.realAsString = real;
    }

    public JObjectValue(String name, ObjectReference object, Field currentfield, JValue jvalue) {
        this(object, name, jvalue.eventthread, currentfield);
        this.alreadyObj = jvalue.alreadyObj;
        this.fieldPath = jvalue.fieldPath.clone();
        this.topLevelObjId = jvalue.topLevelObjId;
        this.realAsString = JValue.NOT_NULL;
    }

    public void setFields(HashMap<String, JValue> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public HashMap<String, JValue> getFields() {
        return this.fieldValues;
    }

    public void addField(String name, JValue value) {
        this.fieldValues.put(name, value);
    }

    public JValue getFieldValue(String name) {
        return this.fieldValues.get(name);
    }

//    @Override
//    protected void extract() {
//        System.out.println(this.object.type().name() + " " + this.name + ":");
//        Iterator iter = fieldValues.entrySet().iterator();
//        while (iter.hasNext()) {
//            Map.Entry<String, JValue> entry = (Entry<String, JValue>) iter.next();
//            // System.out.println("[name] "+ entry.getKey() + " [type] "+
//            // entry.getValue().value.type().name());
//            entry.getValue().acceptExtract(extractVisitor);
//        }
//        if (this.staticFieldValues.size() > 0)
//            System.out.println("[static]");
//        iter = staticFieldValues.entrySet().iterator();
//        while (iter.hasNext()) {
//            Map.Entry<String, JValue> entry = (Entry<String, JValue>) iter.next();
//            // System.out.println("[name] "+ entry.getKey() + " [type] "+
//            // entry.getValue().value.type().name());
//            entry.getValue().acceptExtract(extractVisitor);
//        }
//    }

//    @Override
//    public void acceptExtract(JExtractVisitor jpa) {
//        jpa.extract(this);
//    }

    @Override
    public void acceptCreate(JCreateVisitor visitor) {
        visitor.create(this);
    }

    protected boolean create() {
        // only consider objectreference type
        if (!(this.object instanceof ObjectReference)) {
            System.out.println("Object is unsupported type " + this.object.type().name());
            return false;
        }

        JField f = new JField(this.type, this.type.name(), this.name, this.currentfield);
        this.fieldPath.addFieldToPath(f);
        if(this.meetFieldDepth())
            return false;

        // recurrence reference occured
        if (this.alreadyObj.containsKey(this.object.uniqueID())) {
            System.out.println(name + this.object.type().name() + "recurrence reference to "
                    + this.alreadyObj.get(this.object.uniqueID()));
            // TODO recurrence reference is not perfect. especially print and
            // compare
            return false;
        }
        this.alreadyObj.put(this.object.uniqueID(), this.object);

      //VMInfo level
        if(!VMInfo.intoReferenceObjectAndContainerElement())
            return true;
        
        try {
            for (Field loopfield : this.type.visibleFields()) {
                if (loopfield.isSynthetic())
                    continue;
                String fieldname = loopfield.name();
                Value fieldvalue = this.object.getValue(loopfield);
                Type fieldtype = loopfield.type();
                JValue jv = createFieldValue(loopfield, fieldname, fieldtype, fieldvalue, this);
                if (loopfield.isStatic())
                    this.staticFieldValues.put(fieldname, jv);
                else
                    this.fieldValues.put(fieldname, jv);
            }
        } catch (ClassNotLoadedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public void createObject() {
        this.acceptCreate(createVisitor);
    }

//    public void extractConditions() {
//        this.acceptExtract(extractVisitor);
//    }

    @Override
    public Value getVmValue() {
        return this.object;
    }
}
