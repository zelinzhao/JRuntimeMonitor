package collect.runtime.information.condition;

import collect.runtime.information.hierarchy.JClass;
import collect.runtime.information.main.ProgramPoint;

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

//   0,   1  ,      2       ,    3
//  ==,OBJECT, package.class, 0/1/2/...
//  !=,OBJECT, package.class, 0/1/2/...
//  <<,OBJECT, package.class, 0/1/2/...
//  >>,OBJECT, package.class, 0/1/2/...
    /**
     * for input condition.
     * @param pp
     * @param rawCond
     */
    public ObjectNumberCondition(ProgramPoint pp, String rawCond){
        this.pointID = pp;
        this.rawCond = rawCond;
        String[] sp = rawCond.replaceAll("\\s", "").split(",");
        this.oper = Operator.getOperator(sp[0]);
        this.jclass = new JClass(sp[2]);
        this.number = Integer.valueOf(sp[3]);
    }
    
    @Override 
    public String toString(){
        String result = super.toString()+",";
        result += type+",";
        result += this.jclass.getClassName()+",";
        result += this.number;
        return result;
    }
    
    @Override
    public boolean equals(Object cond){
        if(!super.equals(cond))
            return false;
        ObjectNumberCondition onc = (ObjectNumberCondition)cond;
        if(this.number!= onc.number)
            return false;
        return this.jclass.equals(onc.jclass);
    }
    @Override
    public int hashCode(){
        return this.oper.hashCode()+this.jclass.hashCode()+this.number;
    }
}
