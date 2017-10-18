package collect.runtime.information.condition;

import collect.runtime.information.value.JValue;

public class FieldValueCondition extends Condition{
    public static final String type = "VALUE";
    private JValue jvalue;
    
    /**
     * for output creation. the operator is default
     * @param jvalue
     */
    public FieldValueCondition(JValue jvalue){
        this.jvalue = jvalue;
        this.oper = Operator.EQUAL;
    }
    
    @Override
    public String toString(){
        String result = super.toString()+",";
        result+=type+",";
        result+=this.jvalue.getTopLevelClassName()+",";
        result+=this.jvalue.getFieldPathAsString()+",";
        result+=this.jvalue.getBottomLevelClassName()+",";
        result+=this.jvalue.getRealValueAsString();
        return result;
    }
}
