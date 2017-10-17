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
        return this.fieldPath.get(0).getTypeName();
    }
    public String getBottomLevelClassName(){
        if(this.fieldPath == null || this.fieldPath.size() == 0)
            return null;
        return this.fieldPath.get(this.fieldPath.size()-1).getTypeName();
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
     * 
     * @return the depth of current bottom field. Used for controlling depth
     */
    public int getDepth(){
        return this.fieldPath.size();
    }
    
    
}
