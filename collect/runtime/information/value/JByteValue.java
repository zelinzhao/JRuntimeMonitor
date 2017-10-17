package collect.runtime.information.value;

import com.sun.jdi.ByteValue;
import com.sun.jdi.Field;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;

public class JByteValue extends JValue {
    ByteValue bytev;
    boolean isWrapper = false;

    byte real;

    public JByteValue(String name, ByteValue value, Field field, JValue jvalue) {
        super(name,field);
        this.bytev = value;
        this.real = value.byteValue();
        this.fieldPath = jvalue.fieldPath.clone();
        
        this.topLevelObjId = jvalue.topLevelObjId;
    }

    public JByteValue(byte real) {
        super();
        this.bytev = null;
        this.real = real;
    }

    public byte getRealValue() {
        return this.real;
    }

    @Override
    protected void extract() {
        System.out.println(this.bytev.type().name() + " " + name + " = " + this.bytev.byteValue());
    }

    @Override
    public void acceptExtract(JExtractVisitor jpa) {
        jpa.extract(this);
    }

    @Override
    protected void create() {
        JField jf = new JField(this.bytev.type(), this.bytev.type().name(), this.name, this.currentfield);
        this.fieldPath.addFieldToPath(jf);
    }

    @Override
    public void acceptCreate(JCreateVisitor visitor) {
        visitor.create(this);
    }

    @Override
    public Value getVmValue() {
        return this.bytev;
    }
    @Override
    public String getRealValueAsString(){
        return String.valueOf(real);
    }
}
