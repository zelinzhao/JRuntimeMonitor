package collect.runtime.information.hierarchy;

import com.sun.jdi.Type;

public abstract class Base implements Cloneable {
    protected final String THIS_OBJ = "this";
    /** the type, get from target vm. this thing belongs to. */
    Type type;
    /** the type name, this thing belongs to. */
    String typename;
    /** the name of this thing, 
     * this name equals to JValue.name.
     * When .*/
    String name;

    protected Base(Type type, String typename, String name) {
        this.type = type;
        this.typename = typename;
        if(name==null)
            this.name = "";
        else
            this.name = name;
    }

    public Type getType() {
        return this.type;
    }

    public String getTypeName() {
        return this.typename;
    }

    public String getName() {
        return this.name;
    }

    
    protected abstract Base clone();
}
