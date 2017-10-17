package collect.runtime.information.value;

import java.util.ArrayList;
import java.util.List;

import com.sun.jdi.ArrayReference;
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

public class JSetValue extends JValue {
    ObjectReference object;
    /**
     * each element in list can be different type; elementType here may be
     * inaccurate;
     */
    Type elementType;
    String genericSign;
    int size = 0;
    private ElementNumberCondition elementNumberCondition;
    public void setElementNumberCondition(ElementNumberCondition enc){
        this.elementNumberCondition = enc;
    }
    public ElementNumberCondition getElementNumberCondition(){
        return this.elementNumberCondition;
    }
    List<JValue> elements = new ArrayList<JValue>();

    public JSetValue(String name, ObjectReference array, Field currentfield, JValue jvalue) {
        super(name, jvalue.eventthread, currentfield);
        this.object = array;
        this.alreadyObj = jvalue.alreadyObj;
        this.fieldPath = jvalue.fieldPath.clone();
        
        this.topLevelObjId = jvalue.topLevelObjId;
    }

    public Type getComponentType() {
        return this.elementType;
    }

//    @Override
//    protected void extract() {
//        System.out.println(object.type().name() + " " + name + " (generic signature is " + genericSign + "):");
//        for (JValue jv : elements) {
//            jv.acceptExtract(extractVisitor);
//        }
//    }

    public static boolean isSetValue(Type type) {
        List<InterfaceType> allinterface = null;
        if (type instanceof ClassType) {
            ClassType classtype = (ClassType) type;
            allinterface = classtype.allInterfaces();
        } else if (type instanceof InterfaceType) {
            InterfaceType interType = (InterfaceType) type;
            allinterface = interType.superinterfaces();
            if (interType.name().equals(java.util.Set.class.getName()))
                return true;
        }
        for (InterfaceType face : allinterface)
            if (face.name().equals(java.util.Set.class.getName()))
                return true;
        return false;
    }

    protected boolean create() {
        if (alreadyObj.containsKey(this.object.uniqueID()))
            // TODO recursive reference
            return false;
        alreadyObj.put(this.object.uniqueID(), this.object);
        try {
            this.genericSign = this.currentfield.genericSignature();
            ClassType classtype = (ClassType) object.type();
            ReferenceType referencetype = object.referenceType();

            JField jf = new JField(referencetype, referencetype.name(), this.name, this.currentfield);
            this.fieldPath.addFieldToPath(jf);
            if(this.meetFieldDepth())
                return false;
            // get size of this set
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
            
            //get iterator
            Method iteratorMethod = classtype.concreteMethodByName("iterator", "()Ljava/util/Iterator;");
            if (iteratorMethod == null) {
                System.out.println("No iterator ()Ljava/util/Iterator; method in this type " + object.type().name());
                return false;
            }
            Value iteratorValue = object.invokeMethod(this.eventthread, iteratorMethod, new ArrayList(),
                    ObjectReference.INVOKE_SINGLE_THREADED);
            Method hasNextMethod = ((ClassType)iteratorValue.type()).concreteMethodByName("hasNext", "()Z");
            Method nextMethod = ((ClassType)iteratorValue.type()).concreteMethodByName("next", "()Ljava/lang/Object;");
            
            int index = 0;
            Value hasNextReturn = ((ObjectReference)iteratorValue).invokeMethod(this.eventthread, hasNextMethod, new ArrayList(),
                    ObjectReference.INVOKE_SINGLE_THREADED);
            while(((BooleanValue)hasNextReturn).booleanValue()){  //has next element
                Value nextReturn = ((ObjectReference)iteratorValue).invokeMethod(this.eventthread, nextMethod, new ArrayList(),
                        ObjectReference.INVOKE_SINGLE_THREADED);
                String name = "["+index+"]";
                JValue jv = createFieldValue(currentfield, name, nextReturn.type(), nextReturn, this);
                this.elements.add(jv);
                this.elementType = nextReturn.type();
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
