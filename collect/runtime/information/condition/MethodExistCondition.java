package collect.runtime.information.condition;

import com.sun.jdi.Method;

import collect.runtime.information.hierarchy.JMethod;
import collect.runtime.information.main.ProgramPoint;

/**
 * 
 * @author Zelin Zhao
 * ExistCondition
 * An MethodExistCondition should be like: package.class-method-methodDescriptor.
 * 
 */
public class MethodExistCondition extends Condition{
    public static final String type = "EXIST";
    private JMethod jmethod;
    
    /**
     * for output creation. the operator is default.
     * @param method
     */
    public MethodExistCondition(Method method){
        this.jmethod = new JMethod(method);
        this.oper = Operator.EXIST;
    }
    
//  0,  1    ,       2      ,  3   ,    4  
//  --,EXIST, package.class, method, descriptor
//  ++,EXIST, package.class, method, descriptor
/**
 * for input creation.
 * @param pointID
 * @param rawCond
 */
 public MethodExistCondition(ProgramPoint pointID, String rawCond){
     this.pointID = pointID;
     this.rawCond = rawCond;
     String[] sp = rawCond.replaceAll("\\s", "").split(",");
     if(sp.length<5) // not a right condition string
         return;
     this.oper = Operator.getOperator(sp[0]);
     this.jmethod = new JMethod(sp[2],sp[3],sp[4]);
     
 }
    
    
    @Override
    public String toString(){
        String result = super.toString()+",";
        result += type+",";
        result += this.jmethod.getClassName()+",";
        result += this.jmethod.getMethodName()+",";
        result += this.jmethod.getMethodDesc();
        return result;
    }
}
