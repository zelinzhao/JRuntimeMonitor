package collect.runtime.information.value;

import com.sun.jdi.Field;
import com.sun.jdi.Type;
import com.sun.jdi.Value;

import collect.runtime.information.hierarchy.JField;
import collect.runtime.information.main.VMInfo;

public class JNullValue extends JValue {
    Type type;

//    public JNullValue(String name, JValue jvalue) {
//        this(null, name, jvalue);
//    }

    public JNullValue(String name,Type type, Field field, JValue jvalue) {
        super(name,field);
        this.type = type;
        if (jvalue != null){
            this.fieldPath = jvalue.fieldPath.clone();
            this.topLevelObjId = jvalue.topLevelObjId;
        }
        this.realAsString = JValue.NULL;
    }

//    @Override
//    public void acceptExtract(JExtractVisitor visitor) {
//        // TODO Auto-generated method stub
//        visitor.extract(this);
//    }

    @Override
    public void acceptCreate(JCreateVisitor visitor) {
        // TODO Auto-generated method stub
        visitor.create(this);
    }

    @Override
    protected boolean create() {
        JField jf = null;
        if (type == null)
            jf = new JField(null, null, this.name, this.currentfield);
        else
            jf = new JField(type, type.name(), name, this.currentfield);
        this.fieldPath.addFieldToPath(jf);
        if(this.meetFieldDepth())
            return false;
        return true;
    }

//    @Override
//    protected void extract() {
//        if (type != null)
//            System.out.print(type.name() + " ");
//        else
//            System.out.print("type is null ");
//        System.out.println(this.name + " value is null");
//    }

//    public void printNull() {
//        extract();
//    }

    @Override
    public Value getVmValue() {
        return null;
    }

}
