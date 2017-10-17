package collect.runtime.information.value;

import com.sun.jdi.Field;
import com.sun.jdi.ShortValue;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;

public class JShortValue extends JValue {
    ShortValue shortv;
    boolean isWrapper = false;
    short real;

    public JShortValue(String name,ShortValue value, Field field, JValue jvalue) {
        super(name,field);
        this.shortv = value;
        this.real = value.shortValue();
        this.fieldPath = jvalue.fieldPath.clone();
        
        this.topLevelObjId = jvalue.topLevelObjId;
    }

    public JShortValue(short real) {
        super();
        this.shortv = null;
        this.real = real;
    }

    public short getRealValue() {
        return this.real;
    }

//    @Override
//    protected void extract() {
//        System.out.println(shortv.type().name() + " " + name + " = " + shortv.shortValue());
//    }

//    @Override
//    public void acceptExtract(JExtractVisitor jpa) {
//        jpa.extract(this);
//    }

    @Override
    protected void create() {
        JField jf = new JField(this.shortv.type(), this.shortv.type().name(), this.name, this.currentfield);
        this.fieldPath.addFieldToPath(jf);
    }

    @Override
    public void acceptCreate(JCreateVisitor visitor) {
        visitor.create(this);
    }

    @Override
    public Value getVmValue() {
        return this.shortv;
    }
    @Override
    public String getRealValueAsString(){
        return String.valueOf(real);
    }
}
