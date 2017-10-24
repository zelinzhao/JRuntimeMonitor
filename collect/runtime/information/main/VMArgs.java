package collect.runtime.information.main;

public class VMArgs{
    private String classpath = "";
    private String mainClass = "";
    private String argvs = "";

    public VMArgs(String classpath, String mainClass, String argvs){
        this.classpath = classpath.trim();
        this.mainClass = mainClass;
        this.argvs = argvs.trim();
    }
    public VMArgs() {
        // TODO Auto-generated constructor stub
    }
    public void setClassPath(String classpath) {
        this.classpath = classpath.trim();
    }

    public String getClassPath() {
        return this.classpath;
    }
    
    /**
     * Used to initialize sub vm.
     * @return "-classpath realClassPath" 
     */
    public String getClassPathOption(){
        return "-classpath "+this.classpath;
    }
    
    public void setMainClass(String mainClass) {
        this.mainClass = mainClass.trim();
    }

    public String getMainClass() {
        return this.mainClass;
    }

    public void addArgv(String argv){
        this.argvs += argv.trim()+" ";
    }
    public void setArgvs(String argvs) {
        this.argvs = argvs.trim();
    }
    public String getArgvs() {
        return this.argvs;
    }
    
    @Override
    public String toString() {
        return "classpath: " + classpath + "\nmainclass: " + mainClass + "\nargvs: " + argvs;
    }
}