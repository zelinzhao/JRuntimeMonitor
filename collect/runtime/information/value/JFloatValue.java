package collect.runtime.information.value;

import com.sun.jdi.FloatValue;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;

public class JFloatValue extends JValue {
    FloatValue floatv;
    boolean isWrapper = false;

    float real;

    public JFloatValue(FloatValue value, String name, JValue jvalue) {
        super(name);
        this.floatv = value;
        this.real = value.floatValue();
        this.fieldPath = jvalue.fieldPath.clone();
    }

    public JFloatValue(float real) {
        super(null);
        this.floatv = null;
        this.real = real;
    }

    public float getRealValue() {
        return this.real;
    }

    protected void print() {
        System.out.println(this.floatv.type().name() + " " + name + " = " + this.floatv.floatValue());
    }

    @Override
    public void acceptPrint(JPrintVisitor jpa) {
        jpa.print(this);
    }

    @Override
    protected void create() {
        JField jf = new JField(this.floatv.type(), this.floatv.type().name(), this.name, null);
        this.fieldPath.addFieldToPath(jf);
        return;
    }

    @Override
    public void acceptCreate(JCreateVisitor visitor) {
        visitor.create(this);
    }

    @Override
    public Value getVmValue() {
        return this.floatv;
    }
}
