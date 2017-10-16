package collect.runtime.information.value;

public class JPrintVisitorImplement implements JPrintVisitor {

    @Override
    public void print(JIntegerValue jiv) {
        jiv.print();
    }

    @Override
    public void print(JStringValue jsv) {
        jsv.print();
    }

    @Override
    public void print(JArrayValue jav) {
        jav.print();
    }

    @Override
    public void print(JObjectValue jov) {
        jov.print();
    }

    @Override
    public void print(JBooleanValue jbv) {
        jbv.print();
    }

    @Override
    public void print(JByteValue jbv) {
        jbv.print();
    }

    @Override
    public void print(JCharValue jcv) {
        jcv.print();
    }

    @Override
    public void print(JDoubleValue jdv) {
        jdv.print();
    }

    @Override
    public void print(JFloatValue jfv) {
        jfv.print();
    }

    @Override
    public void print(JLongValue jlv) {
        jlv.print();
    }

    @Override
    public void print(JShortValue jsv) {
        jsv.print();
    }

    @Override
    public void print(JListValue jlv) {
        jlv.print();
    }

    @Override
    public void print(JSetValue jsv) {
        jsv.print();
    }

    @Override
    public void print(JMapValue jmv) {
        jmv.print();
    }

    @Override
    public void print(JNullValue jnv) {
        // TODO Auto-generated method stub
        jnv.print();
    }

}
