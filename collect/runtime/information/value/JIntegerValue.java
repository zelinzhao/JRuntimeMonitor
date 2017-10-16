package collect.runtime.information.value;

import com.sun.jdi.IntegerValue;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;

public class JIntegerValue extends JValue {
    // int integer;
    IntegerValue integer;
    boolean isWrapper = false;
    int real;

    public JIntegerValue(IntegerValue value, String name, JValue jvalue) {
        super(name);
        this.integer = value;
        this.real = value.intValue();
        this.fieldPath = jvalue.fieldPath.clone();
    }

    public JIntegerValue(int real) {
        super(null);
        this.integer = null;
        this.real = real;
    }

    public int getRealValue() {
        return this.real;
    }

    protected void print() {
        System.out.println(integer.type().name() + " " + name + " = " + integer.intValue());
    }

    @Override
    public void acceptPrint(JPrintVisitor jpa) {
        jpa.print(this);
    }

    @Override
    protected void create() {
        JField jf = new JField(this.integer.type(), this.integer.type().name(), this.name, null);
        this.fieldPath.addFieldToPath(jf);
        return;
    }

    @Override
    public void acceptCreate(JCreateVisitor visitor) {
        visitor.create(this);
    }

    @Override
    public Value getVmValue() {
        return this.integer;
    }
}
