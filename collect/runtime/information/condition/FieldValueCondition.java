package collect.runtime.information.condition;

import collect.runtime.information.hierarchy.JFieldPath;
import collect.runtime.information.main.ProgramPoint;
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
    
//   0,  1  ,       2      ,      3        ,       4       ,        5
//  ==,VALUE, package.class, the.field.path, the.field.type, 0/1/2.3/string/c/null/not_null...
//  !=,VALUE, package.class, the.field.path, the.field.type, 0/1/2.3/string/c/null/not_null...
//  <<,VALUE, package.class, the.field.path, the.field.type, 0/1/2.3/string/c/null/not_null...
//  >>,VALUE, package.class, the.field.path, the.field.type, 0/1/2.3/string/c/null/not_null...
    public FieldValueCondition(ProgramPoint pp, String rawCond){
        this.pointID = pp;
        this.rawCond = rawCond;
        String[] sp = rawCond.replaceAll("\\s", "").split(",");
        this.oper = Operator.getOperator(sp[0]);
        JFieldPath jfp = new JFieldPath(sp[2],sp[3],sp[4]);
        JValue jv = JValue.createValueFromInput(sp[4], sp[5]);
        jv.setFieldPath(jfp);
        this.jvalue = jv;
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
    
    @Override
    public boolean equals(Object cond){
        if (!super.equals(cond))
            return false;
        if(cond == null || !(cond instanceof FieldValueCondition))
            return false;
        FieldValueCondition fvc = (FieldValueCondition)cond;
        return this.jvalue.equals(fvc.jvalue);
    }
    @Override
    public int hashCode(){
        return this.oper.hashCode()+this.jvalue.hashCode();
    }
}
