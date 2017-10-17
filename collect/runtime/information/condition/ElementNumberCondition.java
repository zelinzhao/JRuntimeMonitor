package collect.runtime.information.condition;

import collect.runtime.information.hierarchy.JFieldPath;
import collect.runtime.information.value.JValue;

public class ElementNumberCondition extends Condition{
    private static final String type = "ELEMENT";
    private JValue jvalue;
    private int number;
    
    /**
     * for output creation. operator is default.
     * @param jvalue
     * @param size
     */
    public ElementNumberCondition(JValue jvalue, int number){
        this.jvalue = jvalue;
        this.number = number;
        this.oper = Operator.EQUAL;
    }
    
    @Override
    public String toString(){
        String result = super.toString()+",";
        result += this.jvalue.getTopLevelClassName()+",";
        result += this.jvalue.getFieldPathAsString()+",";
        result += this.jvalue.getBottomLevelClassName()+",";
        result += this.number;
        return result;
    }
}
