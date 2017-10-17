package collect.runtime.information.value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sun.jdi.BooleanValue;
import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.ClassType;
import com.sun.jdi.Field;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.IntegerValue;
import com.sun.jdi.InterfaceType;
import com.sun.jdi.InvalidTypeException;
import com.sun.jdi.InvocationException;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Type;
import com.sun.jdi.Value;

import collect.runtime.information.condition.ElementNumberCondition;
import collect.runtime.information.hierarchy.JField;
import collect.runtime.information.main.VMInfo;

public class JMapValue extends JValue {
    ObjectReference object;

    Type keyType;
    Type valueType;
    String genericSign;
    int size;
    private ElementNumberCondition elementNumberCondition;
    public void setElementNumberCondition(ElementNumberCondition enc){
        this.elementNumberCondition = enc;
    }
    public ElementNumberCondition getElementNumberCondition(){
        return this.elementNumberCondition;
    }
    
    /** key and value are put in order and correlated*/
    List<JValue> keyElements = new ArrayList<JValue>();
    /** key and value are put in order and correlated*/
    List<JValue> valueElements = new ArrayList<JValue>();
    
    HashMap<JValue, JValue> entryElements = new HashMap<JValue, JValue>();

//    public JMapValue(ObjectReference object, ThreadReference eventthread, Field field) {
//        super(field.name(), eventthread, field);
//        this.object = object;
//    }

    public JMapValue(String name,ObjectReference array, Field currentfield, JValue jvalue) {
        super(name, jvalue.eventthread, currentfield);
        this.object = array;
        this.alreadyObj = jvalue.alreadyObj;
        this.fieldPath = jvalue.fieldPath.clone();
        
        this.topLevelObjId = jvalue.topLevelObjId;
    }

    public Type getKeyType() {
        return this.keyType;
    }

    public Type getValueType() {
        return this.valueType;
    }

//    @Override
//    protected void extract() {
//        System.out.println(object.type().name() + " " + name + " (generic signature is " + genericSign + "):");
//        Iterator iter = entryElements.entrySet().iterator();
//        while (iter.hasNext()) {
//            Map.Entry<JValue, JValue> entry = (Entry<JValue, JValue>) iter.next();
//            System.out.println("[key]:");
//            entry.getKey().acceptExtract(extractVisitor);
//            System.out.println("[value]:");
//            entry.getValue().acceptExtract(extractVisitor);
//        }
//    }

    public static boolean isMapValue(Type type) {
        List<InterfaceType> allinterface = null;
        if (type instanceof ClassType) {
            ClassType classtype = (ClassType) type;
            allinterface = classtype.allInterfaces();
        } else if (type instanceof InterfaceType) {
            InterfaceType interType = (InterfaceType) type;
            allinterface = interType.superinterfaces();
            if (interType.name().equals(java.util.Map.class.getName()))
                return true;
        }
        for (InterfaceType face : allinterface)
            if (face.name().equals(java.util.Map.class.getName()))
                return true;
        return false;
    }

    protected boolean create() {
        if (alreadyObj.containsKey(this.object.uniqueID()))
            // TODO recursive reference
            return false;
        alreadyObj.put(this.object.uniqueID(), this.object);

        JField jf = new JField(this.object.type(), this.object.type().name(), this.name, this.currentfield);
        this.fieldPath.addFieldToPath(jf);
        if(this.meetFieldDepth())
            return false;

        try {
            this.genericSign = this.currentfield.genericSignature();
            ClassType classtype = (ClassType) object.type();
            ReferenceType referencetype = object.referenceType();
            // get size of this map
            Method sizemethod = classtype.concreteMethodByName("size", "()I");
            if (sizemethod == null) {
                System.out.println("No size ()I method in this type " + object.type().name());
                return false;
            }
            Value sizevalue = object.invokeMethod(this.eventthread, sizemethod, new ArrayList(),
                    ObjectReference.INVOKE_SINGLE_THREADED);
            this.size = ((IntegerValue) sizevalue).intValue();
          //VMInfo level
            if(!VMInfo.intoReferenceObjectAndContainerElement())
                return true;

            // keySet method
            Method keySetMethod = classtype.concreteMethodByName("keySet", "()Ljava/util/Set;");
            if (keySetMethod == null) {
                System.out.println("cannot find keySet method, signature is ()Ljava/util/Set;, for " + name
                        + ", which type is " + classtype.name());
                return false;
            }
            // get method
            Method getMethod = classtype.concreteMethodByName("get", "(Ljava/lang/Object;)Ljava/lang/Object;");
            if (getMethod == null) {
                System.out.println("cannot find get method, signature is (Ljava/lang/Object;)Ljava/lang/Object;, for "
                        + name + ", which type is " + classtype.name());
                return false;
            }
            // get key set
            Value keySetValue = object.invokeMethod(eventthread, keySetMethod, new ArrayList(),
                    ObjectReference.INVOKE_SINGLE_THREADED);
            //get iterator method
            Method iteratorMethod = ((ClassType)keySetValue.type()).concreteMethodByName("iterator", "()Ljava/util/Iterator;");
            //get iterator value
            Value iteratorValue = ((ObjectReference)keySetValue).invokeMethod(this.eventthread, iteratorMethod,
                    new ArrayList(), ObjectReference.INVOKE_SINGLE_THREADED);
            //get hasNext method
            Method hasNextMethod = ((ClassType)iteratorValue.type()).concreteMethodByName("hasNext", "()Z");
            //get next method
            Method nextMethod = ((ClassType)iteratorValue.type()).concreteMethodByName("next", "()Ljava/lang/Object;");
            //iterator key and value
            int index = 0;
            Value hasNextReturn = ((ObjectReference)iteratorValue).invokeMethod(this.eventthread, hasNextMethod, new ArrayList(),
                    ObjectReference.INVOKE_SINGLE_THREADED);
            while(((BooleanValue)hasNextReturn).booleanValue()){  //has next element
                Value nextReturn_key = ((ObjectReference)iteratorValue).invokeMethod(this.eventthread, nextMethod, 
                        new ArrayList(), ObjectReference.INVOKE_SINGLE_THREADED);
                List<Value> arguments = new ArrayList<Value>();
                arguments.add(nextReturn_key);
                
                Value getReturn_value = object.invokeMethod(this.eventthread, getMethod, arguments,
                        ObjectReference.INVOKE_SINGLE_THREADED);
                JValue jv_key = createFieldValue(currentfield, "["+index+".key]", nextReturn_key.type(), nextReturn_key, this);
                JValue jv_value = createFieldValue(currentfield, "["+index+".value]", getReturn_value.type(), getReturn_value, this);
                
                this.keyElements.add(jv_key);
                this.keyType = nextReturn_key.type();
                
                this.valueElements.add(jv_value);
                this.valueType = getReturn_value.type();
                
                this.entryElements.put(jv_key, jv_value);
                
                index++;
                hasNextReturn = ((ObjectReference)iteratorValue).invokeMethod(this.eventthread, hasNextMethod, new ArrayList(),
                        ObjectReference.INVOKE_SINGLE_THREADED);
            }
        } catch (InvalidTypeException | ClassNotLoadedException | IncompatibleThreadStateException
                | InvocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

//    @Override
//    public void acceptExtract(JExtractVisitor jpa) {
//        jpa.extract(this);
//    }

    public void acceptCreate(JCreateVisitor jcv) {
        jcv.create(this);
    }

    @Override
    public Value getVmValue() {
        return this.object;
    }
    @Override
    public String getRealValueAsString(){
        return NOT_NULL;
    }
    public int getSize(){
        return this.size;
    }
}
