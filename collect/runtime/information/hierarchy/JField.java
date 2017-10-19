package collect.runtime.information.hierarchy;

import com.sun.jdi.Field;
import com.sun.jdi.Type;

public class JField extends Base {
    private Field field;

    public JField(Type type, String className, String name, Field field) {
        super(type, className, name);
        this.field = field;
    }
    
    /**
     * for input creation.
     * @param className
     * @param name
     */
    public JField(String className, String name){
        super(null, className, name);
    }

    public Field getField() {
        return this.field;
    }

    @Override
    public JField clone() {
        String tn = new String(className);
        String n = new String(name);
        return new JField(type, tn, n, field);
    }
    
    /**
     * @return field name if this field is not null, or return ""
     */
    public String getFieldAsString(){
        return this.name;
    }
}
