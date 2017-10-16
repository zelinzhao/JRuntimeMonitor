package collect.runtime.information.value;

import com.sun.jdi.ShortValue;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;

public class JShortValue extends JValue {
    ShortValue shortv;
    boolean isWrapper = false;
    short real;

    public JShortValue(ShortValue value, String name, JValue jvalue) {
        super(name);
        this.shortv = value;
        this.real = value.shortValue();
        this.fieldPath = jvalue.fieldPath.clone();
    }

    public JShortValue(short real) {
        super(null);
        this.shortv = null;
        this.real = real;
    }

    public short getRealValue() {
        return this.real;
    }

    protected void print() {
        System.out.println(shortv.type().name() + " " + name + " = " + shortv.shortValue());
    }

    @Override
    public void acceptPrint(JPrintVisitor jpa) {
        jpa.print(this);
    }

    @Override
    protected void create() {
        JField jf = new JField(this.shortv.type(), this.shortv.type().name(), this.name, null);
        this.fieldPath.addFieldToPath(jf);
        return;
    }

    @Override
    public void acceptCreate(JCreateVisitor visitor) {
        visitor.create(this);
    }

    @Override
    public Value getVmValue() {
        return this.shortv;
    }
}
