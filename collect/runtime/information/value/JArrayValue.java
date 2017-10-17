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

import collect.runtime.information.condition.ElementNumberCondition;
import collect.runtime.information.hierarchy.JField;

public class JArrayValue extends JValue {
    ArrayReference array;
    Type elementType;
    
    private ElementNumberCondition elementNumberCondition;
    public void setElementNumberCondition(ElementNumberCondition enc){
        this.elementNumberCondition = enc;
    }
    public ElementNumberCondition getElementNumberCondition(){
        return this.elementNumberCondition;
    }

    List<JValue> elements = new ArrayList<JValue>();

    public JArrayValue(ArrayReference array, String name, Field currentfield, JValue jvalue) {
        super(name,jvalue.eventthread,currentfield);
        this.array = array;
        this.alreadyObj = jvalue.alreadyObj;
        this.fieldPath = jvalue.fieldPath.clone();
        
        this.topLevelObjId = jvalue.topLevelObjId;
    }

    public Type getElementType() {
        return this.elementType;
    }

    public int getLength() {
        return array.length();
    }

    @Override
    protected void extract() {
        System.out.println(array.type().name() + " " + name + ":");
        for (JValue jv : elements) {
            jv.acceptExtract(extractVisitor);
        }
    }
    protected void create() {
        try {
            if (alreadyObj.containsKey(this.array.uniqueID()))
                // TODO recursive reference
                return;
            alreadyObj.put(this.array.uniqueID(), this.array);
            ArrayType arraytype = (ArrayType) array.referenceType();

            JField jf = new JField(arraytype, arraytype.name(), this.name, this.currentfield);
            this.fieldPath.addFieldToPath(jf);

            this.elementType = arraytype.componentType();
            int index = 0;
            for (Value elementvalue : array.getValues()) {
                String name = "[" + index + "]";
                JValue jv = createFieldValue(currentfield, name, elementType, elementvalue, this);
                this.elements.add(jv);
                index++;
            }
        } catch (ClassNotLoadedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void acceptExtract(JExtractVisitor jpa) {
        jpa.extract(this);
    }

    public void acceptCreate(JCreateVisitor jcv) {
        jcv.create(this);
    }

    @Override
    public Value getVmValue() {
        return this.array;
    }
    @Override
    public String getRealValueAsString(){
        return NOT_NULL;
    }
}
