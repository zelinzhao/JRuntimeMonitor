package collect.runtime.information.hierarchy;

import com.sun.jdi.Field;
import com.sun.jdi.Type;

public class JField extends Base {
    private Field field;

    public JField(Type type, String typename, String name, Field field) {
        super(type, typename, name);
        this.field = field;
    }

    public Field getField() {
        return this.field;
    }

    @Override
    public JField clone() {
        String tn = new String(typename);
        String n = new String(name);
        return new JField(type, tn, n, field);
    }
    
    /**
     * @return field name if this field is not null, or return ""
     */
    public String getFieldAsString(){
//        if(this.name == null || this.name.length()==0)
//            return THIS_OBJ;
        return this.name;
    }
}
