package collect.runtime.information.condition;

import com.sun.jdi.Method;

/**
 * 
 * @author Zelin Zhao
 * ExistCondition
 * An MethodExistCondition should be like: package.class-method-methodDescriptor.
 * 
 */
public class MethodExistCondition extends Condition{
    private static final String type = "EXIST";
    private Method method;
    private String methodName;
    private String methodDesc;
    
    public MethodExistCondition(Method method){
        this.className = method.declaringType().name();
        this.method = method;
        this.methodName = method.name();
        this.methodDesc = method.signature();
        this.oper = Operator.EXIST;
    }
    
    @Override
    public String toString(){
        String result = "";
        result += this.getOperString()+",";
        result += type+",";
        result += this.className+",";
        result += this.methodName+",";
        result += this.methodDesc;
        return result;
    }
}
