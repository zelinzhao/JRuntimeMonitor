package collect.runtime.information.value;

import collect.runtime.information.condition.ElementNumberCondition;
import collect.runtime.information.condition.FieldValueCondition;
import collect.runtime.information.main.VMInfo;

public class JCreateVisitorImplement implements JCreateVisitor {

    @Override
    public void create(JIntegerValue jiv) {
        if (jiv.create()) {
            jiv.setCondition(new FieldValueCondition(jiv));
            VMInfo.outputConditions.add(jiv.getCondition());
        }
    }

    @Override
    public void create(JStringValue jsv) {
        if (jsv.create()) {
            jsv.setCondition(new FieldValueCondition(jsv));
            VMInfo.outputConditions.add(jsv.getCondition());
        }
    }

    @Override
    public void create(JArrayValue jav) {
        if (jav.create()) {
            jav.setCondition(new FieldValueCondition(jav));
            VMInfo.outputConditions.add(jav.getCondition());

            jav.setElementNumberCondition(new ElementNumberCondition(jav, jav.getLength()));
            VMInfo.outputConditions.add(jav.getElementNumberCondition());
        }
    }

    @Override
    public void create(JObjectValue jov) {
        if (jov.create()) {
            jov.setCondition(new FieldValueCondition(jov));
            VMInfo.outputConditions.add(jov.getCondition());
            // TODO: if this is recurrence, this condition will be added for two
            // times.
        }
    }

    @Override
    public void create(JBooleanValue jbv) {
        if (jbv.create()) {
            jbv.setCondition(new FieldValueCondition(jbv));
            VMInfo.outputConditions.add(jbv.getCondition());
        }
    }

    @Override
    public void create(JByteValue jbv) {
        if (jbv.create()) {
            jbv.setCondition(new FieldValueCondition(jbv));
            VMInfo.outputConditions.add(jbv.getCondition());
        }
    }

    @Override
    public void create(JCharValue jcv) {
        if (jcv.create()) {
            jcv.setCondition(new FieldValueCondition(jcv));
            VMInfo.outputConditions.add(jcv.getCondition());
        }
    }

    @Override
    public void create(JDoubleValue jdv) {
        if (jdv.create()) {
            jdv.setCondition(new FieldValueCondition(jdv));
            VMInfo.outputConditions.add(jdv.getCondition());
        }
    }

    @Override
    public void create(JFloatValue jfv) {
        if (jfv.create()) {
            jfv.setCondition(new FieldValueCondition(jfv));
            VMInfo.outputConditions.add(jfv.getCondition());
        }
    }

    @Override
    public void create(JLongValue jlv) {
        if (jlv.create()) {
            jlv.setCondition(new FieldValueCondition(jlv));
            VMInfo.outputConditions.add(jlv.getCondition());
        }
    }

    @Override
    public void create(JShortValue jsv) {
        if (jsv.create()) {
            jsv.setCondition(new FieldValueCondition(jsv));
            VMInfo.outputConditions.add(jsv.getCondition());
        }
    }

    @Override
    public void create(JListValue jlv) {
        if (jlv.create()) {
            jlv.setCondition(new FieldValueCondition(jlv));
            VMInfo.outputConditions.add(jlv.getCondition());

            jlv.setElementNumberCondition(new ElementNumberCondition(jlv, jlv.getSize()));
            VMInfo.outputConditions.add(jlv.getElementNumberCondition());
        }
    }

    @Override
    public void create(JSetValue jsv) {
        if (jsv.create()) {
            jsv.setCondition(new FieldValueCondition(jsv));
            VMInfo.outputConditions.add(jsv.getCondition());

            jsv.setElementNumberCondition(new ElementNumberCondition(jsv, jsv.getSize()));
            VMInfo.outputConditions.add(jsv.getElementNumberCondition());
        }
    }

    @Override
    public void create(JMapValue jmv) {
        if (jmv.create()) {
            jmv.setCondition(new FieldValueCondition(jmv));
            VMInfo.outputConditions.add(jmv.getCondition());

            jmv.setElementNumberCondition(new ElementNumberCondition(jmv, jmv.getSize()));
            VMInfo.outputConditions.add(jmv.getElementNumberCondition());
        }

    }

    @Override
    public void create(JNullValue jnv) {
        if (jnv.create()) {
            jnv.setCondition(new FieldValueCondition(jnv));
            VMInfo.outputConditions.add(jnv.getCondition());
        }
    }

}
