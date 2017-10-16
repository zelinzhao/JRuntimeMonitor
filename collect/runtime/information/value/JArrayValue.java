package collect.runtime.information.value;

import java.util.ArrayList;
import java.util.List;

import com.sun.jdi.ArrayReference;
import com.sun.jdi.ArrayType;
import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.Field;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.PrimitiveValue;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Type;
import com.sun.jdi.Value;
import com.sun.jdi.VoidValue;

import collect.runtime.information.hierarchy.JField;

public class JArrayValue extends JValue {
    ArrayReference array;
    Type elementType;

    List<JValue> elements = new ArrayList<JValue>();

    public JArrayValue(ArrayReference array, String name, ThreadReference eventthread, Field field){
        super(name, eventthread, field);
        this.array = array;
    }

    public JArrayValue(ArrayReference array, String name, Field currentfield, JValue jvalue) {
        this(array, name, jvalue.eventthread, currentfield);
        this.alreadyObj = jvalue.alreadyObj;
        this.fieldPath = jvalue.fieldPath.clone();
    }

    public Type getElementType() {
        return this.elementType;
    }

    public int getLength() {
        return array.length();
    }

    @Override
    protected void print() {
        System.out.println(array.type().name() + " " + name + ":");
        for (JValue jv : elements) {
            jv.acceptPrint(printvisitor);
        }
    }

    protected void create() {
        try {
            if (alreadyObj.containsKey(this.array.uniqueID()))
                // TODO recursive reference
                return;
            alreadyObj.put(this.array.uniqueID(), this.array);
            ArrayType arraytype = (ArrayType) array.referenceType();

            JField jf = new JField(arraytype, arraytype.name(), this.name, null);
            this.fieldPath.addFieldToPath(jf);

            this.elementType = arraytype.componentType();
            int index = 0;
            for (Value elementvalue : array.getValues()) {
                String name = "[" + index + "]";
                JValue jv = null;
                if (elementvalue == null) // create null value
                    jv = createReference(elementType, elementvalue, name, currentfield, this);
                else if (elementvalue instanceof PrimitiveValue) {
                    jv = createPrimitive(elementvalue, name, this);
                } else if (elementvalue instanceof ObjectReference) {
                    jv = createReference(elementvalue.type(), elementvalue, name, currentfield, this);
                } else if (elementvalue instanceof VoidValue) {
                    System.out.println("Object unsupported type " + elementType.name());
                }
                this.elements.add(jv);
                index++;
            }
        } catch (ClassNotLoadedException e) {
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
        return this.array;
    }
}
