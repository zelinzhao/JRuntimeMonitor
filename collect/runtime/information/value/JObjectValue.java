package collect.runtime.information.value;

import java.util.HashMap;
import java.util.Iterator;
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

import collect.runtime.information.hierarchy.JField;

public class JObjectValue extends JValue {
    private ObjectReference object;
    private HashMap<String, JValue> fieldValues = new HashMap<String, JValue>();
    private HashMap<String, JValue> staticFieldValues = new HashMap<String, JValue>();

    public JObjectValue(ObjectReference value, String name, ThreadReference eventthread, Field field) {
        super(name, eventthread, field);
        this.object = value;
    }

    public JObjectValue(ObjectReference object, String name, Field currentfield, JValue jvalue) {
        this(object, name, jvalue.eventthread, currentfield);
        this.alreadyObj = jvalue.alreadyObj;
        this.fieldPath = jvalue.fieldPath.clone();
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

    @Override
    protected void print() {
        System.out.println(this.object.type().name() + " " + this.name + ":");
        Iterator iter = fieldValues.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, JValue> entry = (Entry<String, JValue>) iter.next();
            // System.out.println("[name] "+ entry.getKey() + " [type] "+
            // entry.getValue().value.type().name());
            entry.getValue().acceptPrint(printvisitor);
        }
        if (this.staticFieldValues.size() > 0)
            System.out.println("[static]");
        iter = staticFieldValues.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, JValue> entry = (Entry<String, JValue>) iter.next();
            // System.out.println("[name] "+ entry.getKey() + " [type] "+
            // entry.getValue().value.type().name());
            entry.getValue().acceptPrint(printvisitor);
        }
    }

    @Override
    public void acceptPrint(JPrintVisitor jpa) {
        jpa.print(this);
    }

    @Override
    public void acceptCreate(JCreateVisitor visitor) {
        visitor.create(this);
    }

    protected void create() {
        // only consider objectreference type
        if (!(this.object instanceof ObjectReference)) {
            System.out.println("Object is unsupported type " + this.object.type().name());
            return;
        }

        ObjectReference objectReference = (ObjectReference) this.object;
        ReferenceType referencetype = objectReference.referenceType();
        JField f = new JField(referencetype, referencetype.name(), this.name, null);
        this.fieldPath.addFieldToPath(f);

        // recurrence reference occured
        if (alreadyObj.containsKey(objectReference.uniqueID())) {
            System.out.println(name + this.object.type().name() + "recurrence reference to "
                    + alreadyObj.get(objectReference.uniqueID()));
            // TODO recurrence reference is not perfect. especially print and
            // compare
            return;
        }
        alreadyObj.put(this.object.uniqueID(), this.object);
        this.alreadyObj.put(objectReference.uniqueID(), objectReference);
        try {
            for (Field loopfield : referencetype.visibleFields()) {
                if (loopfield.isSynthetic())
                    continue;
                String fieldname = loopfield.name();
                Value fieldvalue = objectReference.getValue(loopfield);
                Type fieldtype = loopfield.type();
                if (fieldtype instanceof PrimitiveType) {
                    JValue jv = createPrimitive(fieldvalue, fieldname, this);
                    if (loopfield.isStatic())
                        this.staticFieldValues.put(fieldname, jv);
                    else
                        this.fieldValues.put(fieldname, jv);
                } else if (fieldtype instanceof ReferenceType) {
                    JValue jv = createReference(fieldtype, fieldvalue, fieldname, loopfield, this);
                    if (loopfield.isStatic())
                        this.staticFieldValues.put(fieldname, jv);
                    else
                        this.fieldValues.put(fieldname, jv);
                    // alreadyObj.put(objectReference.uniqueID(),
                    // objectReference);
                    // if (objectReference instanceof StringReference) {
                    // alreadyObj.remove(objectReference.uniqueID());
                    // }
                } else if (fieldtype instanceof VoidValue) {
                    System.out.println(fieldname + " type is void");
                }
            }
        } catch (ClassNotLoadedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void createObject() {
        this.acceptCreate(createvisitor);
    }

    public void printObject() {
        this.acceptPrint(printvisitor);
    }

    @Override
    public Value getVmValue() {
        return this.object;
    }
}
