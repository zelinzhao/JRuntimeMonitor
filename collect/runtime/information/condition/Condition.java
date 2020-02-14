package collect.runtime.information.condition;

import collect.runtime.information.main.ProgramPoint;

enum Operator{
    EXIST, NOT_EXIST, 
    EQUAL, NOT_EQUAL,
    LESS_THAN, GREATER_THAN;
    
    private static final String exist = "++";
    private static final String not_exist = "--";
    private static final String equal = "==";
    private static final String not_equal = "!=";
    private static final String less_than = "<<";
    private static final String greater_than = ">>";
    
    public static Operator getOperator (String oper){
        switch (oper){
            case exist: return EXIST;
            case not_exist: return NOT_EXIST;
            case equal: return EQUAL;
            case not_equal: return NOT_EQUAL;
            case less_than: return LESS_THAN;
            case greater_than: return GREATER_THAN;
            default: return null;
        }
    }
    @Override
    public String toString(){
        switch (this){
            case EXIST: return exist;
            case NOT_EXIST: return not_exist;
            case EQUAL: return equal;
            case NOT_EQUAL: return not_equal;
            case LESS_THAN: return less_than;
            case GREATER_THAN: return greater_than;
            default: return "??";
        }
    }
}

/**
 * @author Zelin Zhao
 * Condition.
 * There may be some conditions for one point. You should pay attention to the output and input format. 
 */
/*output format:
 *      @@,package.class:lineno
 *      @@,package.class-method-descriptor:enter/exit
 *          (e.g. the point id)
 *      ++,EXIST, package.class, method, descriptor
 *          (e.g. existing a method in stack at this point)
 *      ==,OBJECT, package.class, 0/1/2/..
 *          (e.g. existing 0/2/3/... objects of this class at this point)
 *      ==,ELEMENT, package.class, the.field.path, the.field.type, 0/1/2/3
 *          (e.g. there are 0/1/2/... element in the field path. the field should be set/list/map/array that has size)
 *      ==,VALUE, package.class, the.field.path, the.field.type, 0/1/2.3/string/c/null/not_null...
 *          (e.g. the field at this field path equals to the VALUE. primitive or their wrapper type should compare to concrete value.
 *          reference type should compare to null.)
 */
/*
 * input format:
 *      @@,package.class:lineno
 *      @@,package.class-method-descriptor:enter/exit
 *          (e.g. the point id)
 *      --,EXIST, package.class, method, descriptor
 *          (e.g. can apply update at this point if the method is NOT active)
 *      ++,EXIST, package.class, method, descriptor
 *          (e.g. can apply update at this point if the method IS active)

 *      ==,OBJECT, package.class, 0/1/2/...
 *          (e.g. can apply update at this point if object number of this class EQUALS to 0/1/2...)
 *      !=,OBJECT, package.class, 0/1/2/...
 *          (e.g. can apply update at this point if object number of this class NOT EQUALS to 0/1/2...)
 *      <<,OBJECT, package.class, 0/1/2/...
 *          (e.g. can apply update at this point if object number of this class LESS THAN to 0/1/2...)
 *      >>,OBJECT, package.class, 0/1/2/...
 *          (e.g. can apply update at this point if object number of this class GREATER THAN to 0/1/2...)
 
 *      ==,ELEMENT, package.class, the.field.path, the.field.type, 0/1/2/3
 *      !=,ELEMENT, package.class, the.field.path, the.field.type, 0/1/2/3
 *      <<,ELEMENT, package.class, the.field.path, the.field.type, 0/1/2/3
 *      >>,ELEMENT, package.class, the.field.path, the.field.type, 0/1/2/3

 *       ==,VALUE, package.class, the.field.path, the.field.type, 0/1/2.3/string/c/null/not_null...
 *       !=,VALUE, package.class, the.field.path, the.field.type, 0/1/2.3/string/c/null/not_null...
 *       <<,VALUE, package.class, the.field.path, the.field.type, 0/1/2.3/string/c/null/not_null...
 *       >>,VALUE, package.class, the.field.path, the.field.type, 0/1/2.3/string/c/null/not_null...
 */
public abstract class Condition {
    protected Operator oper;
    
    /** For creating conditions from input*/
    protected ProgramPoint pointID;
    /** For creating conditions from input*/
    protected String rawCond;
    
    @Override
    public String toString(){
        return this.oper.toString();
    }
    
    @Override
    public boolean equals(Object cond){
        if(cond == null || !(cond instanceof Condition))
            return false;
        if(this.oper != ((Condition)cond).oper)
            return false;
        return true;
    }
    
    /**
     * Exclude {@code cond} from {@code this} condition
     * @param cond excluded cond
     * @return new condition that excludes {@code cond}
     */
//    public abstract Condition exclude(Condition cond);
    
    /**
     * Include {@code cond} to {@code this} condition
     * @param cond included cond
     * @return new condition that includes {@code cond}
     */
//    public abstract Condition include(Condition cond);
}
