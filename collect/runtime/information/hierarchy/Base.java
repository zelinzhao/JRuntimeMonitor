package collect.runtime.information.hierarchy;

import com.sun.jdi.Type;

public abstract class Base implements Cloneable {
    /** the type, get from target vm */
    Type type;
    /** the type name */
    String typename;
    /** the name of this thing */
    String name;

    protected Base(Type type, String typename, String name) {
        this.type = type;
        this.typename = typename;
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
