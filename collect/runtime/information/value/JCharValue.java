package collect.runtime.information.value;

import com.sun.jdi.CharValue;
import com.sun.jdi.Field;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;

public class JCharValue extends JValue {
    CharValue charv;
    boolean isWrapper = false;

    char real;

    public JCharValue(String name, CharValue value, Field field, JValue jvalue) {
        super(name,field);
        this.charv = value;
        this.real = value.charValue();
        this.fieldPath = jvalue.fieldPath.clone();
        
        this.topLevelObjId = jvalue.topLevelObjId;
    }

    public JCharValue(char real) {
        super();
        this.charv = null;
        this.real = real;
    }

    public char getRealValue() {
        return this.real;
    }

    @Override
    protected void extract() {
        System.out.println(this.charv.type().name() + " " + name + " = " + this.charv.charValue());
    }

    @Override
    public void acceptExtract(JExtractVisitor jpa) {
        jpa.extract(this);
    }

    @Override
    protected void create() {
        JField jf = new JField(this.charv.type(), this.charv.type().name(), this.name, this.currentfield);
        this.fieldPath.addFieldToPath(jf);
    }

    @Override
    public void acceptCreate(JCreateVisitor visitor) {
        visitor.create(this);
    }

    @Override
    public Value getVmValue() {
        return this.charv;
    }
    
    @Override
    public String getRealValueAsString(){
        return String.valueOf(real);
    }
}
