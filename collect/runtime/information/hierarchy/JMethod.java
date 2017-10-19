package collect.runtime.information.hierarchy;

import java.util.List;

import com.sun.jdi.Method;
import com.sun.jdi.Type;

public class JMethod extends Base {
    private Method method;
    private String methodDesc;
//    private Type returnType;
//    private List<Type> arguments;

    public JMethod(Method method){
        super(method.declaringType(), method.declaringType().name(), method.name());
        this.method = method;
        this.methodDesc = method.signature();
    }

    public JMethod(String className, String methodName, String methodDesc){
        super(null,className,methodName);
        this.methodDesc = methodDesc;
    }
    
    public JMethod clone() {
        // TODO need to implement clone
        return this;
    }
    
    public String getMethodDesc(){
        return this.methodDesc;
    }
    public String getMethodName(){
        return super.getName();
    }
    
}
