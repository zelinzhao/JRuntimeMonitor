package collect.runtime.information.value;

import java.util.ArrayList;
import java.util.List;

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

public class JListValue extends JValue {
    ObjectReference object;
    /**
     * each element in list can be different type; elementType here may be
     * inaccurate;
     */
    Type elementType;
    String genericSign;
    int size = 0;

    List<JValue> elements = new ArrayList<JValue>();

    public JListValue(ObjectReference object, String name, ThreadReference eventthread, Field field) {
        super(name, eventthread, field);
        this.object = object;
    }

    public JListValue(ObjectReference array, String name, Field currentfield, JValue jvalue) {
        this(array, name, jvalue.eventthread, currentfield);
        this.alreadyObj = jvalue.alreadyObj;
        this.fieldPath = jvalue.fieldPath.clone();
    }

    public Type getComponentType() {
        return this.elementType;
    }

    @Override
    protected void print() {
        System.out.println(object.type().name() + " " + name + " (generic signature is " + genericSign + "):");
        int index = 0;
        for (JValue jv : elements) {
            jv.acceptPrint(printvisitor);
        }
    }

    public static boolean isListValue(Type type) {
        List<InterfaceType> allinterface = null;
        if (type instanceof ClassType) {
            ClassType classtype = (ClassType) type;
            allinterface = classtype.allInterfaces();
        } else if (type instanceof InterfaceType) {
            InterfaceType interType = (InterfaceType) type;
            allinterface = interType.superinterfaces();
            if (interType.name().equals(java.util.List.class.getName()))
                return true;
        }
        for (InterfaceType face : allinterface)
            if (face.name().equals(java.util.List.class.getName()))
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

            // get size of this list
            Method sizemethod = classtype.concreteMethodByName("size", "()I");
            if (sizemethod == null) {
                System.out.println("No size ()I method in this type " + object.type().name());
                return;
            }
            Value sizevalue = object.invokeMethod(this.eventthread, sizemethod, new ArrayList(),
                    ObjectReference.INVOKE_SINGLE_THREADED);
            this.size = ((IntegerValue) sizevalue).intValue();
            // get each element
            Method getmethod = classtype.concreteMethodByName("get", "(I)Ljava/lang/Object;");
            if (getmethod == null) {
                System.out.println("cannot find get method, signature is (I)Ljava/lang/Object;, for " + name
                        + ", which type is " + referencetype.name());
                return;
            }
            for (int i = 0; i < size; i++) {
                IntegerValue intvalue = this.object.virtualMachine().mirrorOf(i);
                List<Value> arguments = new ArrayList<Value>();
                arguments.add(intvalue);
                Value element = object.invokeMethod(this.eventthread, getmethod, arguments,
                        ObjectReference.INVOKE_SINGLE_THREADED);
                JValue jv = null;
                if (element == null) {
                    jv = createReference(null, element, "[" + i + "]", currentfield, this);
                } else {
                    if (this.elementType == null)
                        this.elementType = element.type();
                    // no primitive in list
                    // if (element instanceof PrimitiveValue)
                    // jv = createPrimitive(element, "[" + i + "]", vmname);
                    // else if (element instanceof ObjectReference)
                    jv = createReference(element.type(), element, "[" + i + "]", currentfield, this);
                }
                this.elements.add(jv);
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

    private int getSize() {
        return size;
    }

    @Override
    public Value getVmValue() {
        return this.object;
    }

}
