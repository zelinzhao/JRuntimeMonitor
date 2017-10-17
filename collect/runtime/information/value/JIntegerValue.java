package collect.runtime.information.value;

import com.sun.jdi.Field;
import com.sun.jdi.IntegerValue;
import com.sun.jdi.Value;

import collect.runtime.information.condition.Condition;
import collect.runtime.information.condition.FieldValueCondition;
import collect.runtime.information.hierarchy.JField;
import collect.runtime.information.main.VMInfo;

public class JIntegerValue extends JValue {
    // int integer;
    IntegerValue integer;
    boolean isWrapper = false;
    int real;
    
    public JIntegerValue(String name, IntegerValue value, Field field, JValue jvalue) {
        super(name,field);
        this.integer = value;
        this.real = value.intValue();
        this.fieldPath = jvalue.fieldPath.clone();
        
        this.topLevelObjId = jvalue.topLevelObjId;
    }

    public JIntegerValue(int real) {
        super();
        this.integer = null;
        this.real = real;
    }

    public int getRealValue() {
        return this.real;
    }

    @Override
    protected void extract() {
        
        System.out.println(integer.type().name() + " " + name + " = " + integer.intValue());
    }

    @Override
    public void acceptExtract(JExtractVisitor jpa) {
        jpa.extract(this);
    }

    @Override
    protected void create() {
        JField jf = new JField(this.integer.type(), this.integer.type().name(), this.name, this.currentfield);
        this.fieldPath.addFieldToPath(jf);
    }

    @Override
    public void acceptCreate(JCreateVisitor visitor) {
        visitor.create(this);
    }

    @Override
    public Value getVmValue() {
        return this.integer;
    }
    @Override
    public String getRealValueAsString(){
        return String.valueOf(real);
    }
}
