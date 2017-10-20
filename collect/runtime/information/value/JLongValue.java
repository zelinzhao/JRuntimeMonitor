package collect.runtime.information.value;

import com.sun.jdi.Field;
import com.sun.jdi.LongValue;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;
import collect.runtime.information.main.VMInfo;

public class JLongValue extends JValue {
    LongValue longv;
    boolean isWrapper = false;
    long real;

    public JLongValue(String name,LongValue value, Field field, JValue jvalue) {
        super(name,field);
        this.longv = value;
        this.real = value.longValue();
        this.fieldPath = jvalue.fieldPath.clone();
        this.topLevelObjId = jvalue.topLevelObjId;
        this.realAsString = String.valueOf(real);
    }

    public JLongValue(long real, boolean isWrapper) {
        super();
        this.longv = null;
        this.real = real;
        this.isWrapper = isWrapper;
        this.realAsString = String.valueOf(real);
    }

    public long getRealValue() {
        return this.real;
    }

//    @Override
//    protected void extract() {
//        System.out.println(longv.type().name() + " " + name + " = " + longv.longValue());
//    }
//
//    @Override
//    public void acceptExtract(JExtractVisitor jpa) {
//        jpa.extract(this);
//    }

    @Override
    protected boolean create() {
        String classname = (this.isWrapper ? java.lang.Long.class.getName() : this.longv.type().name());
        JField jf = new JField(this.longv.type(), classname, this.name, this.currentfield);
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
        return this.longv;
    }
}
