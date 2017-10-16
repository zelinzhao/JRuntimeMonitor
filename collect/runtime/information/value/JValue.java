package collect.runtime.information.value;

import java.util.HashMap;

import com.sun.jdi.ArrayReference;
import com.sun.jdi.ArrayType;
import com.sun.jdi.BooleanValue;
import com.sun.jdi.ByteValue;
import com.sun.jdi.CharValue;
import com.sun.jdi.DoubleValue;
import com.sun.jdi.Field;
import com.sun.jdi.FloatValue;
import com.sun.jdi.IntegerValue;
import com.sun.jdi.LongValue;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ShortValue;
import com.sun.jdi.StringReference;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Type;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JFieldPath;

public abstract class JValue implements JPrintAccept, JCreateAccept {
    protected static JPrintVisitor printvisitor = new JPrintVisitorImplement();
    protected static JCreateVisitor createvisitor = new JCreateVisitorImplement();
    HashMap<Long, ObjectReference> alreadyObj = new HashMap<Long, ObjectReference>();
    protected boolean transformed = false;
    /**
     * Primitive and their wrapper, String, only need value, name, vmname
     */
    /** object name or the field name */
    protected String name;
    protected JFieldPath fieldPath = new JFieldPath();

    /**
     * other type need vm, eventthread currentfield
     */
    protected ThreadReference eventthread; // for current event
    protected Field currentfield; // for current field

    public JValue(String name, ThreadReference eventthread, Field currentfield) {
        this.name = name;
        this.eventthread = eventthread;
        this.currentfield = currentfield;
    }
    // public JValue(Value value, String name, String vmname, ThreadReference
    // eventthread, Field currentfield) {
    // this.value = value;
    // this.vmname = vmname;
    // this.name = name;
    // this.eventthread = eventthread;
    // this.currentfield = currentfield;
    // }

    /**
     * for Primitive, Primitive wrapper and String
     * @param name
     */
    public JValue(String name) {
        this(name, null, null);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public JFieldPath getFieldPath() {
        return fieldPath;
    }

    /**
     * do not use this from externally
     */
    protected abstract void print();

    /**
     * do not use this from externally
     */
    protected abstract void create();

    public abstract Value getVmValue();

    protected static JValue createPrimitive(Value value, String name, JValue jvalue) {
        if (value instanceof BooleanValue) {
            BooleanValue booleanv = (BooleanValue) value;
            JBooleanValue jbv = new JBooleanValue(booleanv, name, jvalue);
            jbv.acceptCreate(createvisitor);
            return jbv;
        } else if (value instanceof ByteValue) {
            ByteValue bytev = (ByteValue) value;
            JByteValue jbv = new JByteValue(bytev, name, jvalue);
            jbv.acceptCreate(createvisitor);
            return jbv;
        } else if (value instanceof CharValue) {
            CharValue charv = (CharValue) value;
            JCharValue jcv = new JCharValue(charv, name, jvalue);
            jcv.acceptCreate(createvisitor);
            return jcv;
        } else if (value instanceof DoubleValue) {
            DoubleValue doublev = (DoubleValue) value;
            JDoubleValue jdv = new JDoubleValue(doublev, name, jvalue);
            jdv.acceptCreate(createvisitor);
            return jdv;
        } else if (value instanceof FloatValue) {
            FloatValue floatv = (FloatValue) value;
            JFloatValue jfv = new JFloatValue(floatv, name, jvalue);
            jfv.acceptCreate(createvisitor);
            return jfv;
        } else if (value instanceof IntegerValue) {
            IntegerValue integervalue = (IntegerValue) value;
            JIntegerValue jiv = new JIntegerValue(integervalue, name, jvalue);
            jiv.acceptCreate(createvisitor);
            return jiv;
        } else if (value instanceof LongValue) {
            LongValue longv = (LongValue) value;
            JLongValue jlv = new JLongValue(longv, name, jvalue);
            jlv.acceptCreate(createvisitor);
            return jlv;
        } else if (value instanceof ShortValue) {
            ShortValue shortv = (ShortValue) value;
            JShortValue jsv = new JShortValue(shortv, name, jvalue);
            jsv.acceptCreate(createvisitor);
            return jsv;
        }
        System.out.println("Unsupported type " + value.type().name());
        return null;
    }

    protected static JValue createReference(Type type, Value value, String name, Field currentfield, JValue jvalue) {
        // HashMap<Long, ObjectReference> alreadyObj, String vmname,
        // ThreadReference eventthread,
        if (value == null) {
            JNullValue jnv = new JNullValue(type, name, jvalue);
            return jnv;
        }
        ObjectReference objectReference = (ObjectReference) value;
        ReferenceType referencetype = (ReferenceType) type;
        String typename = type.name();

        // array type object
        if (referencetype instanceof ArrayType) {
            ArrayReference arrayvalue = (ArrayReference) objectReference;
            JArrayValue jav = new JArrayValue(arrayvalue, name, currentfield, jvalue);
            jav.acceptCreate(createvisitor);
            return jav;
        } else if (typename.equals(java.lang.String.class.getName())) {
            // string object
            StringReference stringvalue = (StringReference) objectReference;
            JStringValue jsv = new JStringValue(stringvalue, name, jvalue);
            jsv.acceptCreate(createvisitor);
            return jsv;
        } else if (typename.equals(java.lang.Boolean.class.getName())) {
            // Boolean wrapper
            Field field = referencetype.fieldByName("value");
            BooleanValue fieldvalue = (BooleanValue) objectReference.getValue(field);
            JBooleanValue jbv = new JBooleanValue(fieldvalue, name, jvalue);
            jbv.isWrapper = true;
            jbv.acceptCreate(createvisitor);
            return jbv;
        } else if (typename.equals(java.lang.Byte.class.getName())) {
            // Byte wrapper
            Field field = referencetype.fieldByName("value");
            ByteValue fieldvalue = (ByteValue) objectReference.getValue(field);
            JByteValue jbv = new JByteValue(fieldvalue, name, jvalue);
            jbv.isWrapper = true;
            jbv.acceptCreate(createvisitor);
            return jbv;
        } else if (typename.equals(java.lang.Character.class.getName())) {
            // Char wrapper
            Field field = referencetype.fieldByName("value");
            CharValue fieldvalue = (CharValue) objectReference.getValue(field);
            JCharValue jcv = new JCharValue(fieldvalue, name, jvalue);
            jcv.isWrapper = true;
            jcv.acceptCreate(createvisitor);
            return jcv;
        } else if (typename.equals(java.lang.Double.class.getName())) {
            // Double wrapper
            Field field = referencetype.fieldByName("value");
            DoubleValue fieldvalue = (DoubleValue) objectReference.getValue(field);
            JDoubleValue jdv = new JDoubleValue(fieldvalue, name, jvalue);
            jdv.isWrapper = true;
            jdv.acceptCreate(createvisitor);
            return jdv;
        } else if (typename.equals(java.lang.Float.class.getName())) {
            // Float wrapper
            Field field = referencetype.fieldByName("value");
            FloatValue fieldvalue = (FloatValue) objectReference.getValue(field);
            JFloatValue jfv = new JFloatValue(fieldvalue, name, jvalue);
            jfv.isWrapper = true;
            jfv.acceptCreate(createvisitor);
            return jfv;
        } else if (typename.equals(java.lang.Integer.class.getName())) {
            // Integer wrapper
            Field field = referencetype.fieldByName("value");
            IntegerValue fieldvalue = (IntegerValue) objectReference.getValue(field);
            JIntegerValue jiv = new JIntegerValue(fieldvalue, name, jvalue);
            jiv.isWrapper = true;
            jiv.acceptCreate(createvisitor);
            return jiv;
        } else if (typename.equals(java.lang.Long.class.getName())) {
            // Long wrapper
            Field field = referencetype.fieldByName("value");
            LongValue fieldvalue = (LongValue) objectReference.getValue(field);
            JLongValue jlv = new JLongValue(fieldvalue, name, jvalue);
            jlv.isWrapper = true;
            jlv.acceptCreate(createvisitor);
            return jlv;
        } else if (typename.equals(java.lang.Short.class.getName())) {
            // Short wrapper
            Field field = referencetype.fieldByName("value");
            ShortValue fieldvalue = (ShortValue) objectReference.getValue(field);
            JShortValue jsv = new JShortValue(fieldvalue, name, jvalue);
            jsv.isWrapper = true;
            jsv.acceptCreate(createvisitor);
            return jsv;
        } else if (JListValue.isListValue(type)) {
            // list object
            JListValue jlv = new JListValue(objectReference, name, currentfield, jvalue);
            jlv.acceptCreate(createvisitor);
            return jlv;
        } else if (JSetValue.isSetValue(type)) {
            // set object
            JSetValue jsv = new JSetValue(objectReference, name, currentfield, jvalue);
            jsv.acceptCreate(createvisitor);
            return jsv;
        } else if (JMapValue.isMapValue(type)) {
            // map object
            JMapValue jmv = new JMapValue(objectReference, name, currentfield, jvalue);
            jmv.acceptCreate(createvisitor);
            return jmv;
        } else {
            // user defined object
            JObjectValue jov = new JObjectValue(objectReference, name, currentfield, jvalue);
            jov.acceptCreate(createvisitor);
            return jov;
        }
        // error info: unsupported type
        // if (type != null)
        // System.out.println("Unsupported type " + type.name());
        // else if (value != null)
        // System.out.println("Unsupported type " + value.type().name());
        // return null;
    }
}
