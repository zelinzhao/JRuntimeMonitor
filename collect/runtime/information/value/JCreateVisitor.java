package collect.runtime.information.value;

public interface JCreateVisitor {
    void create(JBooleanValue jbv);

    void create(JByteValue jbv);

    void create(JCharValue jcv);

    void create(JDoubleValue jdv);

    void create(JFloatValue jfv);

    void create(JIntegerValue jiv);

    void create(JLongValue jlv);

    void create(JShortValue jsv);

    void create(JStringValue jsv);

    void create(JObjectValue jov);

    void create(JArrayValue jav);

    void create(JListValue jlv);

    void create(JSetValue jsv);

    void create(JMapValue jmv);

    void create(JNullValue jnv);

}
