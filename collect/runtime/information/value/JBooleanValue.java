package collect.runtime.information.value;

import com.sun.jdi.BooleanValue;
import com.sun.jdi.Field;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;
import collect.runtime.information.main.VMInfo;

public class JBooleanValue extends JValue {
    BooleanValue booleanv;
    boolean isWrapper = false;
    boolean real;

    public JBooleanValue(String name, BooleanValue value, Field field, JValue jvalue) {
        super(name, field);
        this.booleanv = value;
        this.real = value.booleanValue();
        this.fieldPath = jvalue.fieldPath.clone();
        this.topLevelObjId = jvalue.topLevelObjId;
        this.realAsString = String.valueOf(real);
    }

    public JBooleanValue(boolean real, boolean isWrapper) {
        super();
        this.booleanv = null;
        this.real = real;
        this.isWrapper = isWrapper;
        this.realAsString = String.valueOf(real);
    }

    public boolean getRealValue() {
        return this.real;
    }

//    @Override
//    protected void extract() {
//        System.out.println(this.booleanv.type().name() + " " + name + " = " + this.booleanv.booleanValue());
//    }
//
//    @Override
//    public void acceptExtract(JExtractVisitor jpa) {
//        jpa.extract(this);
//    }

    @Override
    protected boolean create() {
        String classname =(this.isWrapper ? java.lang.Boolean.class.getName() : this.booleanv.type().name());
        JField jf = new JField(this.booleanv.type(), classname, this.name, this.currentfield);
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
        return this.booleanv;
    }
}
