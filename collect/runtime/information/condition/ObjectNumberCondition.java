package collect.runtime.information.condition;

import collect.runtime.information.hierarchy.JClass;

public class ObjectNumberCondition extends Condition{
    private static final String type = "OBJECT";
    private int number;
    
    public ObjectNumberCondition(JClass jclass){
        this.className = jclass.getTypeName();
        this.number = jclass.getInstanceNumber();
        this.oper = Operator.EQUAL;
    }
    
    @Override 
    public String toString(){
        String result = "";
        result += this.getOperString()+",";
        result += type+",";
        result += this.className+",";
        result += this.number;
        return result;
    }
}
