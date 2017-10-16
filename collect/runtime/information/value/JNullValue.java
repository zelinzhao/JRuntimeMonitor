package collect.runtime.information.value;

import com.sun.jdi.Type;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;

public class JNullValue extends JValue {
    Type type;

    public JNullValue(String name, JValue jvalue) {
        this(null, name, jvalue);
    }

    public JNullValue(Type type, String name, JValue jvalue) {
        super(name);
        this.type = type;
        if (jvalue != null)
            this.fieldPath = jvalue.fieldPath.clone();
    }

    @Override
    public void acceptPrint(JPrintVisitor visitor) {
        // TODO Auto-generated method stub
        visitor.print(this);
    }

    @Override
    public void acceptCreate(JCreateVisitor visitor) {
        // TODO Auto-generated method stub
        visitor.create(this);
    }

    @Override
    protected void create() {
        JField jf = null;
        if (type == null)
            jf = new JField(null, null, this.name, null);
        else
            jf = new JField(type, type.name(), name, null);
        this.fieldPath.addFieldToPath(jf);
        return;
    }

    @Override
    protected void print() {
        if (type != null)
            System.out.print(type.name() + " ");
        else
            System.out.print("type is null ");
        System.out.println(this.name + " value is null");
    }

    public void printNull() {
        print();
    }

    @Override
    public Value getVmValue() {
        return null;
    }

}
