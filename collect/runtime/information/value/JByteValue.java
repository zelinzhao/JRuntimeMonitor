package collect.runtime.information.value;

import com.sun.jdi.ByteValue;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;

public class JByteValue extends JValue {
    ByteValue bytev;
    boolean isWrapper = false;

    byte real;

    public JByteValue(ByteValue value, String name, JValue jvalue) {
        super(name);
        this.bytev = value;
        this.real = value.byteValue();
        this.fieldPath = jvalue.fieldPath.clone();
    }

    public JByteValue(byte real) {
        super(null);
        this.bytev = null;
        this.real = real;
    }

    public byte getRealValue() {
        return this.real;
    }

    protected void print() {
        System.out.println(this.bytev.type().name() + " " + name + " = " + this.bytev.byteValue());
    }

    @Override
    public void acceptPrint(JPrintVisitor jpa) {
        jpa.print(this);
    }

    @Override
    protected void create() {
        JField jf = new JField(this.bytev.type(), this.bytev.type().name(), this.name, null);
        this.fieldPath.addFieldToPath(jf);
        return;
    }

    @Override
    public void acceptCreate(JCreateVisitor visitor) {
        visitor.create(this);
    }

    @Override
    public Value getVmValue() {
        return this.bytev;
    }
}
