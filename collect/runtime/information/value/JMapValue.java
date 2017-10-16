package collect.runtime.information.value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import collect.runtime.information.hierarchy.JField;

public class JMapValue extends JValue {
    ObjectReference object;

    Type keyType;
    Type valueType;
    String genericSign;
    int size;

    List<JValue> keyElements = new ArrayList<JValue>();
    List<JValue> valueElements = new ArrayList<JValue>();

    HashMap<JValue, JValue> entryElements = new HashMap<JValue, JValue>();

    public JMapValue(ObjectReference object, String name, ThreadReference eventthread, Field field) {
        super(name, eventthread, field);
        this.object = object;
    }

    public JMapValue(ObjectReference array, String name, Field currentfield, JValue jvalue) {
        this(array, name, jvalue.eventthread, currentfield);
        this.alreadyObj = jvalue.alreadyObj;
        this.fieldPath = jvalue.fieldPath.clone();
    }

    public Type getKeyType() {
        return this.keyType;
    }

    public Type getValueType() {
        return this.valueType;
    }

    @Override
    protected void print() {
        System.out.println(object.type().name() + " " + name + " (generic signature is " + genericSign + "):");
        Iterator iter = entryElements.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<JValue, JValue> entry = (Entry<JValue, JValue>) iter.next();
            System.out.println("[key]:");
            entry.getKey().acceptPrint(printvisitor);
            System.out.println("[value]:");
            entry.getValue().acceptPrint(printvisitor);
        }
    }

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

    protected void create() {
        if (alreadyObj.containsKey(this.object.uniqueID()))
            // TODO recursive reference
            return;
        alreadyObj.put(this.object.uniqueID(), this.object);

        JField jf = new JField(this.object.type(), this.object.type().name(), this.name, null);
        this.fieldPath.addFieldToPath(jf);
        try {
            this.genericSign = this.currentfield.genericSignature();
            ClassType classtype = (ClassType) object.type();
            ReferenceType referencetype = object.referenceType();
            // get size of this map
            Method sizemethod = classtype.concreteMethodByName("size", "()I");
            if (sizemethod == null) {
                System.out.println("No size ()I method in this type " + object.type().name());
                return;
            }
            Value sizevalue = object.invokeMethod(this.eventthread, sizemethod, new ArrayList(),
                    ObjectReference.INVOKE_SINGLE_THREADED);
            this.size = ((IntegerValue) sizevalue).intValue();

            System.out.println("map size: " + this.size);

            // keySet method
            Method keySetMethod = classtype.concreteMethodByName("keySet", "()Ljava/util/Set;");
            if (keySetMethod == null) {
                System.out.println("cannot find keySet method, signature is ()Ljava/util/Set;, for " + name
                        + ", which type is " + classtype.name());
                return;
            }

            // get method
            Method getMethod = classtype.concreteMethodByName("get", "(Ljava/lang/Object;)Ljava/lang/Object;");
            if (getMethod == null) {
                System.out.println("cannot find get method, signature is (Ljava/lang/Object;)Ljava/lang/Object;, for "
                        + name + ", which type is " + classtype.name());
                return;
            }
            // get key set
            Value keySetValue = object.invokeMethod(eventthread, keySetMethod, new ArrayList(),
                    ObjectReference.INVOKE_SINGLE_THREADED);
            JSetValue jKeySetValue = (JSetValue) createReference(keySetValue.type(), keySetValue, name, currentfield,
                    this);
            this.keyElements = jKeySetValue.elements;
            for (JValue jOneKeyValue : this.keyElements) {
                if (this.keyType == null)
                    this.keyType = jKeySetValue.elementType;
                List<Value> arguments = new ArrayList<Value>();
                arguments.add(jOneKeyValue.getVmValue());
                Value oneValueValue = object.invokeMethod(eventthread, getMethod, arguments,
                        ObjectReference.INVOKE_SINGLE_THREADED);
                JValue jOneValueValue;
                if (oneValueValue == null) {
                    jOneValueValue = createReference(null, oneValueValue, jOneKeyValue.name, currentfield, this);
                } else {
                    jOneValueValue = createReference(oneValueValue.type(), oneValueValue, jOneKeyValue.name,
                            currentfield, this);
                    if (this.valueType == null)
                        this.valueType = jOneValueValue.getVmValue().type();
                }
                this.valueElements.add(jOneValueValue);
                this.entryElements.put(jOneKeyValue, jOneValueValue);
            }

        } catch (InvalidTypeException | ClassNotLoadedException | IncompatibleThreadStateException
                | InvocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void acceptPrint(JPrintVisitor jpa) {
        jpa.print(this);
    }

    public void acceptCreate(JCreateVisitor jcv) {
        jcv.create(this);
    }

    @Override
    public Value getVmValue() {
        return this.object;
    }
}
