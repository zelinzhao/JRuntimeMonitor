package collect.runtime.information.hierarchy;

import java.util.List;

import com.sun.jdi.Method;
import com.sun.jdi.Type;

public class JMethod extends Base {
    private Method method;
    private Type returnType;
    private List<Type> arguments;

    /**
     * 
     * @param type, where this method locate
     * @param typename, where this method locate
     * @param name, method name
     */
    public JMethod(Type type, String typename, String name) {
        super(type, typename, name);
        // TODO Auto-generated constructor stub
    }
    public JMethod(Method method){
        super(method.declaringType(), method.declaringType().name(), method.name());
        this.method = method;
    }

    public JMethod clone() {
        // TODO need to implement clone
        return this;
    }
}
