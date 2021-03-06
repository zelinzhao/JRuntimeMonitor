package collect.runtime.information.condition;

import collect.runtime.information.hierarchy.JFieldPath;
import collect.runtime.information.main.ProgramPoint;
import collect.runtime.information.value.JValue;

public class ElementNumberCondition extends Condition{
    public static final String type = "ELEMENT";
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
    
//     0,  1    ,       2      ,      3        ,       4       ,   5
//    ==,ELEMENT, package.class, the.field.path, the.field.type, 0/1/2/3
//    !=,ELEMENT, package.class, the.field.path, the.field.type, 0/1/2/3
//    <<,ELEMENT, package.class, the.field.path, the.field.type, 0/1/2/3
//    >>,ELEMENT, package.class, the.field.path, the.field.type, 0/1/2/3
    /**
     * for input creation.
     */
    public ElementNumberCondition(ProgramPoint pointID, String rawCond){
        this.pointID = pointID;
        this.rawCond = rawCond;
        String[] sp = rawCond.replaceAll("\\s", "").split(",");
        if(sp.length<6) // not a right condition string
            return;
        this.oper = Operator.getOperator(sp[0]);
        JFieldPath jfp = new JFieldPath(sp[2],sp[3],sp[4]);
        JValue jv = JValue.createValueFromInput(sp[4], sp[5]);
        jv.setFieldPath(jfp);
        this.jvalue = jv;
        this.number = Integer.valueOf(sp[5]);
    }
    
    @Override
    public String toString(){
        String result = super.toString()+",";
        result += type+",";
        result += this.jvalue.getTopLevelClassName()+",";
        result += this.jvalue.getFieldPathAsString()+",";
        result += this.jvalue.getBottomLevelClassName()+",";
        result += this.number;
        return result;
    }
    
    /**
     * comparing the number, 
     * comparing the fieldpath (i.e., top level's classname, bottom level's class and fieldname)
     * @param cond
     * @return
     */
    @Override 
    public boolean equals(Object cond){
        if(!super.equals(cond))
            return false;
        if(cond == null || !(cond instanceof ElementNumberCondition))
            return false;
        ElementNumberCondition enc = (ElementNumberCondition) cond;
        if(this.number!= enc.number)
            return false;
        //for this condition, we only need to compare the field path.
        //the JValue.equals method also compares the real value.
        return this.jvalue.getFieldPath().equals(enc.jvalue.getFieldPath());
    }
    
    @Override
    public int hashCode(){
        return this.oper.hashCode()+this.jvalue.getFieldPath().hashCode()+this.number;
    }
}
