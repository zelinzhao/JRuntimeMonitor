package collect.runtime.information.hierarchy;

import java.util.LinkedList;

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
}
