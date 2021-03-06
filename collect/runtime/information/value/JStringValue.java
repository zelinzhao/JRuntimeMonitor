package collect.runtime.information.value;

import com.sun.jdi.Field;
import com.sun.jdi.StringReference;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;
import collect.runtime.information.main.VMInfo;

public class JStringValue extends JValue {

    StringReference string;
    String real;

    public JStringValue(String name,StringReference string, Field field, JValue jvalue) {
        super(name, field);
        this.string = string;
        this.real = string.value();
        this.fieldPath = jvalue.fieldPath.clone();
        this.topLevelObjId = jvalue.topLevelObjId;
        this.realAsString = String.valueOf(real);
    }

    public JStringValue(String real) {
        super();
        this.string = null;
        this.real = real;
        this.realAsString = String.valueOf(real);
    }

    public String getRealValue() {
        return this.real;
    }

//    protected void extract() {
//        System.out.println(string.type().name() + " " + name + " = " + string.value());
//    }

//    @Override
//    public void acceptExtract(JExtractVisitor jpa) {
//        jpa.extract(this);
//    }

    @Override
    protected boolean create() {
        JField jf = new JField(this.string.type(), this.string.type().name(), this.name, this.currentfield);
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
        return this.string;
    }
}
