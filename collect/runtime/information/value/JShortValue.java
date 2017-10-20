package collect.runtime.information.value;

import com.sun.jdi.Field;
import com.sun.jdi.ShortValue;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;
import collect.runtime.information.main.VMInfo;

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
        this.realAsString = String.valueOf(real);
    }

    public JShortValue(short real, boolean isWrapper) {
        super();
        this.shortv = null;
        this.real = real;
        this.isWrapper = isWrapper;
        this.realAsString = String.valueOf(real);
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
    protected boolean create() {
        String classname = (this.isWrapper ? java.lang.Short.class.getName() : this.shortv.type().name());
        JField jf = new JField(this.shortv.type(), classname, this.name, this.currentfield);
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
        return this.shortv;
    }
}
