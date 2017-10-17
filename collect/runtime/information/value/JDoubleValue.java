package collect.runtime.information.value;

import com.sun.jdi.DoubleValue;
import com.sun.jdi.Field;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;

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
    }

    public JDoubleValue(double real) {
        super();
        this.doublev = null;
        this.real = real;
    }

    public double getRealValue() {
        return this.real;
    }

    @Override
    protected void extract() {
        System.out.println(this.doublev.type().name() + " " + name + " = " + this.doublev.doubleValue());
    }

    @Override
    public void acceptExtract(JExtractVisitor jpa) {
        jpa.extract(this);
    }

    @Override
    protected void create() {
        JField jf = new JField(this.doublev.type(), this.doublev.type().name(), this.name, this.currentfield);
        this.fieldPath.addFieldToPath(jf);
    }

    @Override
    public void acceptCreate(JCreateVisitor visitor) {
        visitor.create(this);
    }

    @Override
    public Value getVmValue() {
        return this.doublev;
    }
    @Override
    public String getRealValueAsString(){
        return String.valueOf(real);
    }
}
