package collect.runtime.information.value;

import com.sun.jdi.DoubleValue;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;

public class JDoubleValue extends JValue {
    DoubleValue doublev;
    boolean isWrapper = false;

    double real;

    public JDoubleValue(DoubleValue value, String name, JValue jvalue) {
        super(name);
        this.doublev = value;
        this.real = value.doubleValue();
        this.fieldPath = jvalue.fieldPath.clone();
    }

    public JDoubleValue(double real) {
        super(null);
        this.doublev = null;
        this.real = real;
    }

    public double getRealValue() {
        return this.real;
    }

    protected void print() {
        System.out.println(this.doublev.type().name() + " " + name + " = " + this.doublev.doubleValue());
    }

    @Override
    public void acceptPrint(JPrintVisitor jpa) {
        jpa.print(this);
    }

    @Override
    protected void create() {
        JField jf = new JField(this.doublev.type(), this.doublev.type().name(), this.name, null);
        this.fieldPath.addFieldToPath(jf);
        return;
    }

    @Override
    public void acceptCreate(JCreateVisitor visitor) {
        visitor.create(this);
    }

    @Override
    public Value getVmValue() {
        return this.doublev;
    }
}
