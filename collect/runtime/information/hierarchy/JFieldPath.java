package collect.runtime.information.hierarchy;

import java.util.LinkedList;

/**
 * Top level object's field is null.
 * While creating a JFieldPath, it looks like: null,field1,field2 
 * @author Zelin Zhao
 */
public class JFieldPath implements Cloneable {
    LinkedList<JField> fieldPath = new LinkedList<JField>();

    public JFieldPath() {
    }

    public JFieldPath(LinkedList<JField> fieldPath) {
        this.fieldPath = fieldPath;
    }
    
    /**
     * for input creation.
     * @param topLevelClass
     * @param fieldPath
     * @param bottomLevelClass
     */
    public JFieldPath(String topLevelClass, String fieldPath, String bottomLevelClass){
        String[] sp = fieldPath.split("\\.");
        if(sp[0].equals("this")) // top level field's name is null or ""
            this.fieldPath.add(new JField(topLevelClass, null));
        for(int i=1; i<sp.length-1; i++)
            this.fieldPath.add(new JField(null, sp[i]));
        if(sp.length>=2)
            this.fieldPath.add(new JField(bottomLevelClass, sp[sp.length-1]));
    }
    

    public JFieldPath clone() {
        LinkedList<JField> clone = new LinkedList<JField>();
        for (JField jf : fieldPath)
            clone.add(jf.clone());
        return new JFieldPath(clone);
    }

    public void addFieldToPath(JField field) {
        this.fieldPath.add(field);
    }
    
    public String getTopLevelClassName(){
        if(this.fieldPath == null || this.fieldPath.size() == 0)
            return null;
        return this.fieldPath.get(0).getClassName();
    }
    public String getBottomLevelClassName(){
        if(this.fieldPath == null || this.fieldPath.size() == 0)
            return null;
        return this.fieldPath.get(this.fieldPath.size()-1).getClassName();
    }
    
    /**
     * @return string representation of this field path like field1.field2.field3.
     * object's name is not included.
     */
    public String getFieldPathAsString(){
        String result = "";
        for(int i=0; i<this.fieldPath.size(); i++){
            String temp = this.fieldPath.get(i).getFieldAsString();
            if(result.length()!=0 && temp!=null && temp.length()!=0)
                result+=".";
            if(i==0 && (temp==null || temp.length()==0))
                result += Base.THIS_OBJ;
            if(temp.length()!=0)
                result+= temp;
        }
        return result;
    }
    
    /**
     * @return the depth of current bottom field. Used for controlling depth
     */
    public int getDepth(){
        return this.fieldPath.size();
    }
    
    public JField getTopField(){
        return this.fieldPath.getFirst();
    }
    public JField getBottomField(){
        return this.fieldPath.getLast();
    }
    
    /**
     * Two field paths, they are equal if:
     *  their top level field's classname are equal (top level field doesn't have field name);
     *  their bottom level field's classname, fieldname are equal;
     *  other fields' name between top and bottom levels are all equal 
     *  (these fields don't have type name if they are created by input data);
     *  If there are array, list, set, their index are ignored.
     *  If there are map, their index are ignored, only consider it's a value or key.
     */
    @Override
    public boolean equals(Object obj){
        if(obj == null || !(obj instanceof JFieldPath))
            return false;
        JFieldPath jfp = (JFieldPath)obj;
        //field depth
        if(this.fieldPath.size() != jfp.fieldPath.size())
            return false;
        //top level class name
        if(!this.getTopLevelClassName().equals(jfp.getTopLevelClassName()))
            return false;
        //bottom level class and field names
        if( !this.fieldPath.getLast().getName().equals(jfp.fieldPath.getLast().getName()) 
                || !this.fieldPath.getLast().getClassName().equals(jfp.fieldPath.getLast().getClassName()))
            return false;
        //other fields' name
        for(int i=1; i<this.fieldPath.size()-1; i++){
            if(!this.fieldPath.get(i).compareFieldName(jfp.fieldPath.get(i)))
                return false;
        }
        return true;
    }
    
    @Override
    public int hashCode(){
        return (this.getTopLevelClassName()
                +this.getFieldPathAsString()
                +this.getBottomLevelClassName()).hashCode();
    }
}
