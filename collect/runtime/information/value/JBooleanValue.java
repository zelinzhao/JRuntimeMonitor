package collect.runtime.information.value;

import com.sun.jdi.BooleanValue;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;

public class JBooleanValue extends JValue {
    BooleanValue booleanv;
    boolean isWrapper = false;
    boolean real;

    public JBooleanValue(BooleanValue value, String name, JValue jvalue) {
        super(name);
        this.booleanv = value;
        this.real = value.booleanValue();
        this.fieldPath = jvalue.fieldPath.clone();
    }

    public JBooleanValue(boolean real) {
        super(null);
        this.booleanv = null;
        this.real = real;
    }

    public boolean getRealValue() {
        return this.real;
    }

    protected void print() {
        System.out.println(this.booleanv.type().name() + " " + name + " = " + this.booleanv.booleanValue());
    }

    @Override
    public void acceptPrint(JPrintVisitor jpa) {
        jpa.print(this);
    }

    @Override
    protected void create() {
        JField jf = new JField(this.booleanv.type(), this.booleanv.type().name(), this.name, null);
        this.fieldPath.addFieldToPath(jf);
        return;
    }

    @Override
    public void acceptCreate(JCreateVisitor visitor) {
        visitor.create(this);
    }

    @Override
    public Value getVmValue() {
        return this.booleanv;
    }
}
