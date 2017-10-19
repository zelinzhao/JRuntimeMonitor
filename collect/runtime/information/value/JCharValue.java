package collect.runtime.information.value;

import com.sun.jdi.CharValue;
import com.sun.jdi.Field;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;
import collect.runtime.information.main.VMInfo;

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

    public JCharValue(char real, boolean isWrapper) {
        super();
        this.charv = null;
        this.real = real;
        this.isWrapper = isWrapper;
    }

    public char getRealValue() {
        return this.real;
    }

//    @Override
//    protected void extract() {
//        System.out.println(this.charv.type().name() + " " + name + " = " + this.charv.charValue());
//    }
//
//    @Override
//    public void acceptExtract(JExtractVisitor jpa) {
//        jpa.extract(this);
//    }

    @Override
    protected boolean create() {
        String classname = (this.isWrapper ? java.lang.Character.class.getName() : this.charv.type().name());
        JField jf = new JField(this.charv.type(), classname , this.name, this.currentfield);
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
        return this.charv;
    }
    
    @Override
    public String getRealValueAsString(){
        return String.valueOf(real);
    }
}
