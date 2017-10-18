package collect.runtime.information.condition;

import collect.runtime.information.hierarchy.JClass;

public class ObjectNumberCondition extends Condition{
    public static final String type = "OBJECT";
    private JClass jclass;
    private int number;
    
    /**
     * for output creation. operator is default.
     * @param jclass
     */
    public ObjectNumberCondition(JClass jclass){
        this.jclass = jclass;
        this.number = jclass.getInstanceNumber();
        this.oper = Operator.EQUAL;
    }
    
    @Override 
    public String toString(){
        String result = super.toString()+",";
        result += type+",";
        result += this.jclass.getTypeName()+",";
        result += this.number;
        return result;
    }
}
