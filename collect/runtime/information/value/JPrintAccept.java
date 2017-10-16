package collect.runtime.information.value;

public interface JPrintAccept {
    /**
     * do not use this from externally
     */
    void acceptPrint(JPrintVisitor visitor);
}
