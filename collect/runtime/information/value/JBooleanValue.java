package collect.runtime.information.value;

import com.sun.jdi.BooleanValue;
import com.sun.jdi.Field;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;

public class JBooleanValue extends JValue {
    BooleanValue booleanv;
    boolean isWrapper = false;
    boolean real;

    public JBooleanValue(String name, BooleanValue value, Field field, JValue jvalue) {
        super(name, field);
        this.booleanv = value;
        this.real = value.booleanValue();
        this.fieldPath = jvalue.fieldPath.clone();
        
        this.topLevelObjId = jvalue.topLevelObjId;
    }

    public JBooleanValue(boolean real) {
        super();
        this.booleanv = null;
        this.real = real;
    }

    public boolean getRealValue() {
        return this.real;
    }

    @Override
    protected void extract() {
        System.out.println(this.booleanv.type().name() + " " + name + " = " + this.booleanv.booleanValue());
    }

    @Override
    public void acceptExtract(JExtractVisitor jpa) {
        jpa.extract(this);
    }

    @Override
    protected void create() {
        JField jf = new JField(this.booleanv.type(), this.booleanv.type().name(), this.name, this.currentfield);
        this.fieldPath.addFieldToPath(jf);
    }

    @Override
    public void acceptCreate(JCreateVisitor visitor) {
        visitor.create(this);
    }

    @Override
    public Value getVmValue() {
        return this.booleanv;
    }
    @Override
    public String getRealValueAsString(){
        return String.valueOf(real);
    }
}
