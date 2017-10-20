package collect.runtime.information.hierarchy;

import com.sun.jdi.Field;
import com.sun.jdi.Type;

public class JField extends Base {
    private static final String indexReg = "\\[[0-9]+\\]";
    private static final String keyReg = "\\[[0-9]+\\.key\\]";
    private static final String valueReg = "\\[[0-9]+\\.value\\]";
    private Field field;

    public JField(Type type, String className, String name, Field field) {
        super(type, className, name);
        this.field = field;
    }

    /**
     * for input creation.
     * 
     * @param className
     * @param name
     */
    public JField(String className, String name) {
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
    public String getFieldAsString() {
        return this.name;
    }

    /**
     * @param jf
     * @return true if their name are equal; false if not;
     */
    public boolean compareFieldName(JField jf) {
        if (jf == null)
            return false;
        if (this.name == null && jf.name == null)
            return true;
        if (this.name != null && jf.name != null) {
            if (this.name.matches(indexReg) && jf.name.matches(indexReg))
                return true;
            if (this.name.matches(keyReg) && jf.name.matches(keyReg))
                return true;
            if (this.name.matches(valueReg) && jf.name.matches(valueReg))
                return true;
            if (this.name.equals(jf.name))
                return true;
        }
        return false;
    }
}
