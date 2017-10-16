package collect.runtime.information.main;

import com.sun.jdi.VirtualMachine;

public abstract class VM {
    protected VirtualMachine vm;
    volatile protected boolean connected = false;
}
