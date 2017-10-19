package collect.runtime.information.hierarchy;

import com.sun.jdi.Type;

public abstract class Base implements Cloneable {
    protected static final String THIS_OBJ = "this";
    /** the type, get from target vm. this thing belongs to. */
    Type type;
    /** the class name, this thing belongs to. 
     * for primitive and string, {@code className} and {@code type} are corresponding.
     * for primitive wrapper type, {@code className} is wrapper type but {@code type} is primitive type.*/
    String className;
    /** the name of this thing, 
     * this name equals to JValue.name.
     * When .*/
    String name;

    protected Base(Type type, String className, String name) {
        this.type = type;
        this.className = className;
        if(name==null)
            this.name = "";
        else
            this.name = name;
    }

    public Type getType() {
        return this.type;
    }

    public String getClassName() {
        return this.className;
    }

    public String getName() {
        return this.name;
    }

    
    protected abstract Base clone();
}
