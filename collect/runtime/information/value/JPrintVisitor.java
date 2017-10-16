package collect.runtime.information.value;

public interface JPrintVisitor {
    
    void print(JBooleanValue jbv);

    void print(JByteValue jbv);

    void print(JCharValue jcv);

    void print(JDoubleValue jdv);

    void print(JFloatValue jfv);

    void print(JIntegerValue jiv);

    void print(JLongValue jlv);

    void print(JShortValue jsv);

    void print(JStringValue jsv);

    void print(JObjectValue jov);

    void print(JArrayValue jav);

    void print(JListValue jlv);

    void print(JSetValue jsv);

    void print(JMapValue jmv);

    void print(JNullValue jnv);
}
