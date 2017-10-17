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
    
    /**
     * for output creation. the operator is default.
     * @param method
     */
    public MethodExistCondition(Method method){
        this.method = method;
        this.oper = Operator.EXIST;
    }
    
    @Override
    public String toString(){
        String result = super.toString()+",";
        result += type+",";
        result += this.method.declaringType().name()+",";
        result += this.method.name()+",";
        result += this.method.signature();
        return result;
    }
}
