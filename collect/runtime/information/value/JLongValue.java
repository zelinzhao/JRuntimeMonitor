package collect.runtime.information.value;

import com.sun.jdi.Field;
import com.sun.jdi.LongValue;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;

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
    }

    public JLongValue(long real) {
        super();
        this.longv = null;
        this.real = real;
    }

    public long getRealValue() {
        return this.real;
    }

    @Override
    protected void extract() {
        System.out.println(longv.type().name() + " " + name + " = " + longv.longValue());
    }

    @Override
    public void acceptExtract(JExtractVisitor jpa) {
        jpa.extract(this);
    }

    @Override
    protected void create() {
        JField jf = new JField(this.longv.type(), this.longv.type().name(), this.name, this.currentfield);
        this.fieldPath.addFieldToPath(jf);
    }

    @Override
    public void acceptCreate(JCreateVisitor visitor) {
        visitor.create(this);
    }

    @Override
    public Value getVmValue() {
        return this.longv;
    }
    @Override
    public String getRealValueAsString(){
        return String.valueOf(real);
    }
}
