package collect.runtime.information.value;

public class JCreateVisitorImplement implements JCreateVisitor {

    @Override
    public void create(JIntegerValue jiv) {
        jiv.create();
    }

    @Override
    public void create(JStringValue jsv) {
        jsv.create();
    }

    @Override
    public void create(JArrayValue jav) {
        jav.create();
    }

    @Override
    public void create(JObjectValue jov) {
        jov.create();
    }

    @Override
    public void create(JBooleanValue jbv) {
        jbv.create();
    }

    @Override
    public void create(JByteValue jbv) {
        jbv.create();
    }

    @Override
    public void create(JCharValue jcv) {
        jcv.create();
    }

    @Override
    public void create(JDoubleValue jdv) {
        jdv.create();
    }

    @Override
    public void create(JFloatValue jfv) {
        jfv.create();
    }

    @Override
    public void create(JLongValue jlv) {
        jlv.create();
    }

    @Override
    public void create(JShortValue jsv) {
        jsv.create();
    }

    @Override
    public void create(JListValue jlv) {
        jlv.create();
    }

    @Override
    public void create(JSetValue jsv) {
        jsv.create();
    }

    @Override
    public void create(JMapValue jmv) {
        jmv.create();
    }

    @Override
    public void create(JNullValue jnv) {
        jnv.create();
    }

}
