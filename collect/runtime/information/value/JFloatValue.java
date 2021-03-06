package collect.runtime.information.value;

import com.sun.jdi.Field;
import com.sun.jdi.FloatValue;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;
import collect.runtime.information.main.VMInfo;

public class JFloatValue extends JValue {
    FloatValue floatv;
    boolean isWrapper = false;

    float real;

    public JFloatValue(String name,FloatValue value, Field field, JValue jvalue) {
        super(name,field);
        this.floatv = value;
        this.real = value.floatValue();
        this.fieldPath = jvalue.fieldPath.clone();
        this.topLevelObjId = jvalue.topLevelObjId;
        this.realAsString = String.valueOf(real);
    }

    public JFloatValue(float real, boolean isWrapper) {
        super();
        this.floatv = null;
        this.real = real;
        this.isWrapper = isWrapper;
        this.realAsString = String.valueOf(real);
    }

    public float getRealValue() {
        return this.real;
    }

//    @Override
//    protected void extract() {
//        System.out.println(this.floatv.type().name() + " " + name + " = " + this.floatv.floatValue());
//    }
//
//    @Override
//    public void acceptExtract(JExtractVisitor jpa) {
//        jpa.extract(this);
//    }

    @Override
    protected boolean create() {
        String classname = (this.isWrapper ? java.lang.Float.class.getName() : this.floatv.type().name());
        JField jf = new JField(this.floatv.type(), classname, this.name, this.currentfield);
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
        return this.floatv;
    }
}
