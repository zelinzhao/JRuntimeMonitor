package collect.runtime.information.value;

import com.sun.jdi.Field;
import com.sun.jdi.FloatValue;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;

public class JFloatValue extends JValue {
    FloatValue floatv;
    boolean isWrapper = false;

    float real;

    public JFloatValue(String name,FloatValue value, Field field, JValue jvalue) {
        super(name,field);
        this.floatv = value;
        this.real = value.floatValue();
        this.fieldPath = jvalue.fieldPath.clone();
        
        this.topLevelObjId = jvalue.topLevelObjId;
    }

    public JFloatValue(float real) {
        super();
        this.floatv = null;
        this.real = real;
    }

    public float getRealValue() {
        return this.real;
    }

    @Override
    protected void extract() {
        System.out.println(this.floatv.type().name() + " " + name + " = " + this.floatv.floatValue());
    }

    @Override
    public void acceptExtract(JExtractVisitor jpa) {
        jpa.extract(this);
    }

    @Override
    protected void create() {
        JField jf = new JField(this.floatv.type(), this.floatv.type().name(), this.name, this.currentfield);
        this.fieldPath.addFieldToPath(jf);
    }

    @Override
    public void acceptCreate(JCreateVisitor visitor) {
        visitor.create(this);
    }

    @Override
    public Value getVmValue() {
        return this.floatv;
    }
    @Override
    public String getRealValueAsString(){
        return String.valueOf(real);
    }
}
