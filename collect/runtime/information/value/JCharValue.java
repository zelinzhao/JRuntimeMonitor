package collect.runtime.information.value;

import com.sun.jdi.CharValue;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;

public class JCharValue extends JValue {
    CharValue charv;
    boolean isWrapper = false;

    char real;

    public JCharValue(CharValue value, String name, JValue jvalue) {
        super(name);
        this.charv = value;
        this.real = value.charValue();
        this.fieldPath = jvalue.fieldPath.clone();
    }

    public JCharValue(char real) {
        super(null);
        this.charv = null;
        this.real = real;
    }

    public char getRealValue() {
        return this.real;
    }

    protected void print() {
        System.out.println(this.charv.type().name() + " " + name + " = " + this.charv.charValue());
    }

    @Override
    public void acceptPrint(JPrintVisitor jpa) {
        jpa.print(this);
    }

    @Override
    protected void create() {
        JField jf = new JField(this.charv.type(), this.charv.type().name(), this.name, null);
        this.fieldPath.addFieldToPath(jf);
        return;
    }

    @Override
    public void acceptCreate(JCreateVisitor visitor) {
        visitor.create(this);
    }

    @Override
    public Value getVmValue() {
        return this.charv;
    }
}
