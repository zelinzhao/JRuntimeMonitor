package collect.runtime.information.value;

import com.sun.jdi.DoubleValue;
import com.sun.jdi.Field;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;
import collect.runtime.information.main.VMInfo;

public class JDoubleValue extends JValue {
    DoubleValue doublev;
    boolean isWrapper = false;

    double real;

    public JDoubleValue(String name, DoubleValue value, Field field, JValue jvalue) {
        super(name,field);
        this.doublev = value;
        this.real = value.doubleValue();
        this.fieldPath = jvalue.fieldPath.clone();
        this.topLevelObjId = jvalue.topLevelObjId;
        this.realAsString = String.valueOf(real);
    }

    public JDoubleValue(double real, boolean isWrapper) {
        super();
        this.doublev = null;
        this.real = real;
        this.isWrapper = isWrapper;
        this.realAsString = String.valueOf(real);
    }

    public double getRealValue() {
        return this.real;
    }

//    @Override
//    protected void extract() {
//        System.out.println(this.doublev.type().name() + " " + name + " = " + this.doublev.doubleValue());
//    }
//
//    @Override
//    public void acceptExtract(JExtractVisitor jpa) {
//        jpa.extract(this);
//    }

    @Override
    protected boolean create() {
        String classname = (this.isWrapper ? java.lang.Double.class.getName() : this.doublev.type().name());
        JField jf = new JField(this.doublev.type(), classname, this.name, this.currentfield);
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
        return this.doublev;
    }
}
