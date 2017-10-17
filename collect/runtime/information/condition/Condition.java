package collect.runtime.information.condition;

import collect.runtime.information.main.ProgramPoint;

enum Operator{
    EXIST, NOT_EXIST, 
    EQUAL, NOT_EQUAL,
    LESS_THAN, GREATER_THAN
}

/**
 * @author Zelin Zhao
 * Condition.
 * There may be some conditions for one point. You should pay attention to the output and input format. 
 */
/*output format:
 *      @@package.class, method, descriptor
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
 *      @@package.class, method, descriptor
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
        switch (oper){
        case EXIST:         return "++"; 
        case NOT_EXIST:     return "--"; 
        case EQUAL:         return "=="; 
        case NOT_EQUAL:     return "!="; 
        case LESS_THAN:     return "<<"; 
        case GREATER_THAN:  return ">>"; 
        default:            return "??";
        }
    }
}
