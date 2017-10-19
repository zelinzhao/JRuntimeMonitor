package collect.runtime.information.value;

import java.util.HashMap;

import com.sun.jdi.ArrayReference;
import com.sun.jdi.ArrayType;
import com.sun.jdi.BooleanType;
import com.sun.jdi.BooleanValue;
import com.sun.jdi.ByteType;
import com.sun.jdi.ByteValue;
import com.sun.jdi.CharType;
import com.sun.jdi.CharValue;
import com.sun.jdi.DoubleType;
import com.sun.jdi.DoubleValue;
import com.sun.jdi.Field;
import com.sun.jdi.FloatType;
import com.sun.jdi.FloatValue;
import com.sun.jdi.IntegerType;
import com.sun.jdi.IntegerValue;
import com.sun.jdi.LongType;
import com.sun.jdi.LongValue;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ShortType;
import com.sun.jdi.ShortValue;
import com.sun.jdi.StringReference;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Type;
import com.sun.jdi.Value;

import collect.runtime.information.condition.Condition;
import collect.runtime.information.hierarchy.JFieldPath;
import collect.runtime.information.main.VMInfo;

public abstract class JValue implements JCreateAccept { // JExtractAccept
    // TODO: there are some container classes needed to be implemented
    // TODO: the wrapper classes, their typename are primitive not wrapper name.
    protected static final String NOT_NULL = "NOT_NULL";
    protected static final String NULL = "NULL";

    // protected static JExtractVisitor extractVisitor = new
    // JExtractVisitorImplement();
    protected static JCreateVisitor createVisitor = new JCreateVisitorImplement();
    HashMap<Long, ObjectReference> alreadyObj = new HashMap<Long, ObjectReference>();
    protected long topLevelObjId;
    /**
     * Primitive and their wrapper, String, only need value, name
     */
    /** object name or the field name */
    protected String name;
    /**
     * field path to access this object. For top level object, this is empty.
     */
    protected JFieldPath fieldPath = new JFieldPath();
    

    public boolean meetFieldDepth() {
        if (VMInfo.DEPTH == 0 || this.fieldPath.getDepth() <= VMInfo.DEPTH)
            return false;
        return true;
    }

    protected Condition condition;

    public void setCondition(Condition cond) {
        this.condition = cond;
    }

    public Condition getCondition() {
        return this.condition;
    }

    /**
     * other type need eventthread currentfield
     */
    protected ThreadReference eventthread; // for current event
    protected Field currentfield; // for current field

    public JValue() {
    }

    public JValue(String name, ThreadReference eventthread, Field currentfield) {
        this.name = name;
        this.eventthread = eventthread;
        this.currentfield = currentfield;
    }

    /**
     * In some cases, the name of element in array, set, list and map, the name
     * is different from field.name().
     * 
     * @param name
     * @param field
     */
    public JValue(String name, Field field) {
        this(name, null, field);
    }

    /**
     * This constructor makes name equals field.name().
     * 
     * @param field
     *            cannot be null
     */
    public JValue(Field field) {
        this(field.name(), field);
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
    public void setFieldPath(JFieldPath jfp){
        this.fieldPath = jfp;
    }

    public long getTopLevelObjId() {
        return this.topLevelObjId;
    }
    // /**
    // * do not use this from externally
    // */
    // protected abstract void extract();

    /**
     * do not use this from externally
     */
    protected abstract boolean create();

    // public abstract Condition getCondition();
    public String getTopLevelClassName() {
        return this.fieldPath.getTopLevelClassName();
    }

    public String getBottomLevelClassName() {
        return this.fieldPath.getBottomLevelClassName();
    }

    public String getFieldPathAsString() {
        return this.fieldPath.getFieldPathAsString();
    }

    public abstract Value getVmValue();

    /**
     * For output conditions.
     * 
     * @return null/not_null if is reference type object, real value as string
     *         if is primitive type object.
     */
    public abstract String getRealValueAsString();

    /**
     * for input creation.
     * 
     * @param typename
     *            primitive or reference type, full class name.
     * @param real
     *            for primitive value, turn {@code real} to corresponding type
     *            value. Pay attention to char type value, only passing the
     *            real[0] to char value. for reference type value, {@code real}
     *            is NULL/NOT_NULL
     * @return JValue
     */
    public static JValue createValueFromInput(String typename, String real) {
        if (typename.equals(java.lang.String.class.getName()))
            // string object
            return new JStringValue(real);
        else if (typename.equals(java.lang.Boolean.class.getName()))
            // Boolean wrapper
            return new JBooleanValue(Boolean.valueOf(real), true);
        else if (typename.equals("boolean"))
            return new JBooleanValue(Boolean.valueOf(real), false);
        else if (typename.equals(java.lang.Byte.class.getName()))
            // Byte wrapper
            return new JByteValue(Byte.valueOf(real), true);
        else if (typename.equals("byte"))
            return new JByteValue(Byte.valueOf(real), false);
        else if (typename.equals(java.lang.Character.class.getName()))
            // Char wrapper
            return new JCharValue(real.charAt(0), true);
        else if(typename.equals("char"))
            return new JCharValue(real.charAt(0), false);
        else if (typename.equals(java.lang.Double.class.getName()))
            // Double wrapper
            return new JDoubleValue(Double.valueOf(real), true);
        else if(typename.equals("double"))
            return new JDoubleValue(Double.valueOf(real), false);
        else if (typename.equals(java.lang.Float.class.getName()))
            // Float wrapper
            return new JFloatValue(Float.valueOf(real), true);
        else if(typename.equals("float"))
            return new JFloatValue(Float.valueOf(real), false);
        else if (typename.equals(java.lang.Integer.class.getName()))
            // Integer wrapper
            return new JIntegerValue(Integer.valueOf(real), true);
        else if(typename.equals("int"))
            return new JIntegerValue(Integer.valueOf(real), false);
        else if (typename.equals(java.lang.Long.class.getName()))
            // Long wrapper
            return new JLongValue(Long.valueOf(real),true);
        else if(typename.equals("long"))
            return new JLongValue(Long.valueOf(real),false);
        else if (typename.equals(java.lang.Short.class.getName()))
            // short value
            return new JShortValue(Short.valueOf(real), true);
        else if(typename.equals("short"))
            return new JShortValue(Short.valueOf(real), false);
        else
            return new JObjectValue(real);
    }

    /**
     * 
     * @param currentField
     * @param fieldName
     *            while this is an element of array,list,map,set, fieldName is
     *            different with currentField.name()
     * @param fieldType
     * @param fieldValue
     * @param fatherObj
     * @return
     */
    protected static JValue createFieldValue(Field currentField, String fieldName, Type fieldType, Value fieldValue,
            JValue fatherObj) {
        if (fieldType instanceof BooleanType) {
            BooleanValue booleanv = (BooleanValue) fieldValue;
            JBooleanValue jbv = new JBooleanValue(fieldName, booleanv, currentField, fatherObj);
            jbv.acceptCreate(createVisitor);
            return jbv;
        } else if (fieldType instanceof ByteType) {
            ByteValue bytev = (ByteValue) fieldValue;
            JByteValue jbv = new JByteValue(fieldName, bytev, currentField, fatherObj);
            jbv.acceptCreate(createVisitor);
            return jbv;
        } else if (fieldType instanceof CharType) {
            CharValue charv = (CharValue) fieldValue;
            JCharValue jcv = new JCharValue(fieldName, charv, currentField, fatherObj);
            jcv.acceptCreate(createVisitor);
            return jcv;
        } else if (fieldType instanceof DoubleType) {
            DoubleValue doublev = (DoubleValue) fieldValue;
            JDoubleValue jdv = new JDoubleValue(fieldName, doublev, currentField, fatherObj);
            jdv.acceptCreate(createVisitor);
            return jdv;
        } else if (fieldType instanceof FloatType) {
            FloatValue floatv = (FloatValue) fieldValue;
            JFloatValue jfv = new JFloatValue(fieldName, floatv, currentField, fatherObj);
            jfv.acceptCreate(createVisitor);
            return jfv;
        } else if (fieldType instanceof IntegerType) {
            IntegerValue integervalue = (IntegerValue) fieldValue;
            JIntegerValue jiv = new JIntegerValue(fieldName, integervalue, currentField, fatherObj);
            jiv.acceptCreate(createVisitor);
            return jiv;
        } else if (fieldType instanceof LongType) {
            LongValue longv = (LongValue) fieldValue;
            JLongValue jlv = new JLongValue(fieldName, longv, currentField, fatherObj);
            jlv.acceptCreate(createVisitor);
            return jlv;
        } else if (fieldType instanceof ShortType) {
            ShortValue shortv = (ShortValue) fieldValue;
            JShortValue jsv = new JShortValue(fieldName, shortv, currentField, fatherObj);
            jsv.acceptCreate(createVisitor);
            return jsv;
            // primitive type ends here
        } else if (fieldValue == null) {
            JNullValue jnv = new JNullValue(fieldName, fieldType, currentField, fatherObj);
            jnv.acceptCreate(createVisitor);
            return jnv;
        } else if (fieldType instanceof ReferenceType) {
            ObjectReference objectReference = (ObjectReference) fieldValue;
            ReferenceType referencetype = (ReferenceType) fieldType;
            String typename = fieldType.name();
            if (referencetype instanceof ArrayType) {
                ArrayReference arrayvalue = (ArrayReference) objectReference;
                JArrayValue jav = new JArrayValue(arrayvalue, fieldName, currentField, fatherObj);
                jav.acceptCreate(createVisitor);
                return jav;
            } else if (typename.equals(java.lang.String.class.getName())) {
                // string object
                StringReference stringvalue = (StringReference) objectReference;
                JStringValue jsv = new JStringValue(fieldName, stringvalue, currentField, fatherObj);
                jsv.acceptCreate(createVisitor);
                return jsv;
            } else if (typename.equals(java.lang.Boolean.class.getName())) {
                // Boolean wrapper
                Field field = referencetype.fieldByName("value");
                BooleanValue fieldvalue = (BooleanValue) objectReference.getValue(field);
                JBooleanValue jbv = new JBooleanValue(fieldName, fieldvalue, currentField, fatherObj);
                jbv.isWrapper = true;
                jbv.acceptCreate(createVisitor);
                return jbv;
            } else if (typename.equals(java.lang.Byte.class.getName())) {
                // Byte wrapper
                Field field = referencetype.fieldByName("value");
                ByteValue fieldvalue = (ByteValue) objectReference.getValue(field);
                JByteValue jbv = new JByteValue(fieldName, fieldvalue, currentField, fatherObj);
                jbv.isWrapper = true;
                jbv.acceptCreate(createVisitor);
                return jbv;
            } else if (typename.equals(java.lang.Character.class.getName())) {
                // Char wrapper
                Field field = referencetype.fieldByName("value");
                CharValue fieldvalue = (CharValue) objectReference.getValue(field);
                JCharValue jcv = new JCharValue(fieldName, fieldvalue, currentField, fatherObj);
                jcv.isWrapper = true;
                jcv.acceptCreate(createVisitor);
                return jcv;
            } else if (typename.equals(java.lang.Double.class.getName())) {
                // Double wrapper
                Field field = referencetype.fieldByName("value");
                DoubleValue fieldvalue = (DoubleValue) objectReference.getValue(field);
                JDoubleValue jdv = new JDoubleValue(fieldName, fieldvalue, currentField, fatherObj);
                jdv.isWrapper = true;
                jdv.acceptCreate(createVisitor);
                return jdv;
            } else if (typename.equals(java.lang.Float.class.getName())) {
                // Float wrapper
                Field field = referencetype.fieldByName("value");
                FloatValue fieldvalue = (FloatValue) objectReference.getValue(field);
                JFloatValue jfv = new JFloatValue(fieldName, fieldvalue, currentField, fatherObj);
                jfv.isWrapper = true;
                jfv.acceptCreate(createVisitor);
                return jfv;
            } else if (typename.equals(java.lang.Integer.class.getName())) {
                // Integer wrapper
                Field field = referencetype.fieldByName("value");
                IntegerValue fieldvalue = (IntegerValue) objectReference.getValue(field);
                JIntegerValue jiv = new JIntegerValue(fieldName, fieldvalue, currentField, fatherObj);
                jiv.isWrapper = true;
                jiv.acceptCreate(createVisitor);
                return jiv;
            } else if (typename.equals(java.lang.Long.class.getName())) {
                // Long wrapper
                Field field = referencetype.fieldByName("value");
                LongValue fieldvalue = (LongValue) objectReference.getValue(field);
                JLongValue jlv = new JLongValue(fieldName, fieldvalue, currentField, fatherObj);
                jlv.isWrapper = true;
                jlv.acceptCreate(createVisitor);
                return jlv;
            } else if (typename.equals(java.lang.Short.class.getName())) {
                // Short wrapper
                Field field = referencetype.fieldByName("value");
                ShortValue fieldvalue = (ShortValue) objectReference.getValue(field);
                JShortValue jsv = new JShortValue(fieldName, fieldvalue, currentField, fatherObj);
                jsv.isWrapper = true;
                jsv.acceptCreate(createVisitor);
                return jsv;
            } else if (JListValue.isListValue(fieldType)) {
                // list object
                JListValue jlv = new JListValue(fieldName, objectReference, currentField, fatherObj);
                jlv.acceptCreate(createVisitor);
                return jlv;
            } else if (JSetValue.isSetValue(fieldType)) {
                // set object
                JSetValue jsv = new JSetValue(fieldName, objectReference, currentField, fatherObj);
                jsv.acceptCreate(createVisitor);
                return jsv;
            } else if (JMapValue.isMapValue(fieldType)) {
                // map object
                JMapValue jmv = new JMapValue(fieldName, objectReference, currentField, fatherObj);
                jmv.acceptCreate(createVisitor);
                return jmv;
            } else {
                // user defined object
                JObjectValue jov = new JObjectValue(fieldName, objectReference, currentField, fatherObj);
                jov.acceptCreate(createVisitor);
                return jov;
            }
        }
        System.out.println("Unsupported type " + fieldType.name());
        return null;
    }

    // protected static JValue createPrimitive(Value value, String name, JValue
    // jvalue) {
    // if (value instanceof BooleanValue) {
    // BooleanValue booleanv = (BooleanValue) value;
    // JBooleanValue jbv = new JBooleanValue(booleanv, name, jvalue);
    // jbv.acceptCreate(createVisitor);
    // return jbv;
    // } else if (value instanceof ByteValue) {
    // ByteValue bytev = (ByteValue) value;
    // JByteValue jbv = new JByteValue(bytev, name, jvalue);
    // jbv.acceptCreate(createVisitor);
    // return jbv;
    // } else if (value instanceof CharValue) {
    // CharValue charv = (CharValue) value;
    // JCharValue jcv = new JCharValue(charv, name, jvalue);
    // jcv.acceptCreate(createVisitor);
    // return jcv;
    // } else if (value instanceof DoubleValue) {
    // DoubleValue doublev = (DoubleValue) value;
    // JDoubleValue jdv = new JDoubleValue(doublev, name, jvalue);
    // jdv.acceptCreate(createVisitor);
    // return jdv;
    // } else if (value instanceof FloatValue) {
    // FloatValue floatv = (FloatValue) value;
    // JFloatValue jfv = new JFloatValue(floatv, name, jvalue);
    // jfv.acceptCreate(createVisitor);
    // return jfv;
    // } else if (value instanceof IntegerValue) {
    // IntegerValue integervalue = (IntegerValue) value;
    // JIntegerValue jiv = new JIntegerValue(integervalue, name, jvalue);
    // jiv.acceptCreate(createVisitor);
    // return jiv;
    // } else if (value instanceof LongValue) {
    // LongValue longv = (LongValue) value;
    // JLongValue jlv = new JLongValue(longv, name, jvalue);
    // jlv.acceptCreate(createVisitor);
    // return jlv;
    // } else if (value instanceof ShortValue) {
    // ShortValue shortv = (ShortValue) value;
    // JShortValue jsv = new JShortValue(shortv, name, jvalue);
    // jsv.acceptCreate(createVisitor);
    // return jsv;
    // }
    // System.out.println("Unsupported type " + value.type().name());
    // return null;
    // }
    //
    // protected static JValue createReference(Type type, Value value, String
    // name, Field currentfield, JValue jvalue) {
    // if (value == null) {
    // JNullValue jnv = new JNullValue(type, name, jvalue);
    // return jnv;
    // }
    // ObjectReference objectReference = (ObjectReference) value;
    // ReferenceType referencetype = (ReferenceType) type;
    // String typename = type.name();
    //
    // // array type object
    // if (referencetype instanceof ArrayType) {
    // ArrayReference arrayvalue = (ArrayReference) objectReference;
    // JArrayValue jav = new JArrayValue(arrayvalue, name, currentfield,
    // jvalue);
    // jav.acceptCreate(createVisitor);
    // return jav;
    // } else if (typename.equals(java.lang.String.class.getName())) {
    // // string object
    // StringReference stringvalue = (StringReference) objectReference;
    // JStringValue jsv = new JStringValue(stringvalue, currentfield, jvalue);
    // jsv.acceptCreate(createVisitor);
    // return jsv;
    // } else if (typename.equals(java.lang.Boolean.class.getName())) {
    // // Boolean wrapper
    // Field field = referencetype.fieldByName("value");
    // BooleanValue fieldvalue = (BooleanValue) objectReference.getValue(field);
    // JBooleanValue jbv = new JBooleanValue(fieldvalue, name, jvalue);
    // jbv.isWrapper = true;
    // jbv.acceptCreate(createVisitor);
    // return jbv;
    // } else if (typename.equals(java.lang.Byte.class.getName())) {
    // // Byte wrapper
    // Field field = referencetype.fieldByName("value");
    // ByteValue fieldvalue = (ByteValue) objectReference.getValue(field);
    // JByteValue jbv = new JByteValue(fieldvalue, name, jvalue);
    // jbv.isWrapper = true;
    // jbv.acceptCreate(createVisitor);
    // return jbv;
    // } else if (typename.equals(java.lang.Character.class.getName())) {
    // // Char wrapper
    // Field field = referencetype.fieldByName("value");
    // CharValue fieldvalue = (CharValue) objectReference.getValue(field);
    // JCharValue jcv = new JCharValue(fieldvalue, name, jvalue);
    // jcv.isWrapper = true;
    // jcv.acceptCreate(createVisitor);
    // return jcv;
    // } else if (typename.equals(java.lang.Double.class.getName())) {
    // // Double wrapper
    // Field field = referencetype.fieldByName("value");
    // DoubleValue fieldvalue = (DoubleValue) objectReference.getValue(field);
    // JDoubleValue jdv = new JDoubleValue(fieldvalue, name, jvalue);
    // jdv.isWrapper = true;
    // jdv.acceptCreate(createVisitor);
    // return jdv;
    // } else if (typename.equals(java.lang.Float.class.getName())) {
    // // Float wrapper
    // Field field = referencetype.fieldByName("value");
    // FloatValue fieldvalue = (FloatValue) objectReference.getValue(field);
    // JFloatValue jfv = new JFloatValue(fieldvalue, name, jvalue);
    // jfv.isWrapper = true;
    // jfv.acceptCreate(createVisitor);
    // return jfv;
    // } else if (typename.equals(java.lang.Integer.class.getName())) {
    // // Integer wrapper
    // Field field = referencetype.fieldByName("value");
    // IntegerValue fieldvalue = (IntegerValue) objectReference.getValue(field);
    // JIntegerValue jiv = new JIntegerValue(fieldvalue, name, jvalue);
    // jiv.isWrapper = true;
    // jiv.acceptCreate(createVisitor);
    // return jiv;
    // } else if (typename.equals(java.lang.Long.class.getName())) {
    // // Long wrapper
    // Field field = referencetype.fieldByName("value");
    // LongValue fieldvalue = (LongValue) objectReference.getValue(field);
    // JLongValue jlv = new JLongValue(fieldvalue, name, jvalue);
    // jlv.isWrapper = true;
    // jlv.acceptCreate(createVisitor);
    // return jlv;
    // } else if (typename.equals(java.lang.Short.class.getName())) {
    // // Short wrapper
    // Field field = referencetype.fieldByName("value");
    // ShortValue fieldvalue = (ShortValue) objectReference.getValue(field);
    // JShortValue jsv = new JShortValue(fieldvalue, name, jvalue);
    // jsv.isWrapper = true;
    // jsv.acceptCreate(createVisitor);
    // return jsv;
    // } else if (JListValue.isListValue(type)) {
    // // list object
    // JListValue jlv = new JListValue(objectReference, name, currentfield,
    // jvalue);
    // jlv.acceptCreate(createVisitor);
    // return jlv;
    // } else if (JSetValue.isSetValue(type)) {
    // // set object
    // JSetValue jsv = new JSetValue(objectReference, name, currentfield,
    // jvalue);
    // jsv.acceptCreate(createVisitor);
    // return jsv;
    // } else if (JMapValue.isMapValue(type)) {
    // // map object
    // JMapValue jmv = new JMapValue(objectReference, name, currentfield,
    // jvalue);
    // jmv.acceptCreate(createVisitor);
    // return jmv;
    // } else {
    // // user defined object
    // JObjectValue jov = new JObjectValue(objectReference, name, currentfield,
    // jvalue);
    // jov.acceptCreate(createVisitor);
    // return jov;
    // }
    // // error info: unsupported type
    // // if (type != null)
    // // System.out.println("Unsupported type " + type.name());
    // // else if (value != null)
    // // System.out.println("Unsupported type " + value.type().name());
    // // return null;
    // }
}
