package collect.runtime.information.value;

import com.sun.jdi.ByteValue;
import com.sun.jdi.Field;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;
import collect.runtime.information.main.VMInfo;

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
        this.realAsString = String.valueOf(real);
    }

    public JByteValue(byte real, boolean isWrapper) {
        super();
        this.bytev = null;
        this.real = real;
        this.isWrapper = isWrapper;
        this.realAsString = String.valueOf(real);
    }

    public byte getRealValue() {
        return this.real;
    }

//    @Override
//    protected void extract() {
//        System.out.println(this.bytev.type().name() + " " + name + " = " + this.bytev.byteValue());
//    }
//
//    @Override
//    public void acceptExtract(JExtractVisitor jpa) {
//        jpa.extract(this);
//    }

    @Override
    protected boolean create() {
        String classname = (this.isWrapper ? java.lang.Byte.class.getName() : this.bytev.type().name());
        JField jf = new JField(this.bytev.type(), classname , this.name, this.currentfield);
        this.fieldPath.addFieldToPath(jf);
        if(this.meetFieldDepth())
            return false;
        return true;
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
