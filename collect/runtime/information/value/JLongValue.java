package collect.runtime.information.value;

import com.sun.jdi.LongValue;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;

public class JLongValue extends JValue {
    LongValue longv;
    boolean isWrapper = false;
    long real;

    public JLongValue(LongValue value, String name, JValue jvalue) {
        super(name);
        this.longv = value;
        this.real = value.longValue();
        this.fieldPath = jvalue.fieldPath.clone();
    }

    public JLongValue(long real) {
        super(null);
        this.longv = null;
        this.real = real;
    }

    public long getRealValue() {
        return this.real;
    }

    protected void print() {
        System.out.println(longv.type().name() + " " + name + " = " + longv.longValue());
    }

    @Override
    public void acceptPrint(JPrintVisitor jpa) {
        jpa.print(this);
    }

    @Override
    protected void create() {
        JField jf = new JField(this.longv.type(), this.longv.type().name(), this.name, null);
        this.fieldPath.addFieldToPath(jf);
        return;
    }

    @Override
    public void acceptCreate(JCreateVisitor visitor) {
        visitor.create(this);
    }

    @Override
    public Value getVmValue() {
        return this.longv;
    }
}
