package collect.runtime.information.value;

import com.sun.jdi.StringReference;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;

public class JStringValue extends JValue {

    StringReference string;
    String real;

    public JStringValue(StringReference string, String name, JValue jvalue) {
        super(name);
        this.string = string;
        this.real = string.value();
        this.fieldPath = jvalue.fieldPath.clone();
    }

    public JStringValue(String real) {
        super(null);
        this.string = null;
        this.real = real;
    }

    public String getRealValue() {
        return this.real;
    }

    protected void print() {
        System.out.println(string.type().name() + " " + name + " = " + string.value());
    }

    @Override
    public void acceptPrint(JPrintVisitor jpa) {
        jpa.print(this);
    }

    @Override
    protected void create() {
        JField jf = new JField(this.string.type(), this.string.type().name(), this.name, null);
        this.fieldPath.addFieldToPath(jf);
        return;
    }

    @Override
    public void acceptCreate(JCreateVisitor visitor) {
        visitor.create(this);
    }

    @Override
    public Value getVmValue() {
        return this.string;
    }
}
