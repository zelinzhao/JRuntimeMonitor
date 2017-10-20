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
    
    @Override
    public boolean equals(Object met){
        if(met ==null || !(met instanceof JMethod))
            return false;
        JMethod jm = (JMethod) met;
        if(this.className.equals(jm.className) 
                && this.name.equals(jm.name)
                && this.methodDesc.equals(jm.methodDesc))
            return true;
        return false;
    }
    
    @Override
    public int hashCode(){
        return (this.className
                +this.name
                +this.methodDesc).hashCode();
    }
}
