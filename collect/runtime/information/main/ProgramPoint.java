package collect.runtime.information.main;

enum PointLoc{
    ENTER, EXIT, LINE, OTHER;
}

public abstract class ProgramPoint {
    protected String packageName ="";
    protected String className ="";
    protected PointLoc loc = PointLoc.OTHER;
    
    public ProgramPoint(String packageName, String className, PointLoc loc){
        this.packageName = packageName;
        this.className = className;
        this.loc = loc;
    }
    
    public void setPackageName(String packageName){
        this.packageName = packageName;
    }
    public void setClassName(String className){
        this.className = className;
    }
    
    public String getPackageName(){
        return this.packageName;
    }
    public String getClassName(){
        return this.className;
    }
    public String getFullClassName(){
        return this.packageName+"."+this.className;
    }
    
    public PointLoc getLoc(){
        return this.loc;
    }
    public boolean isLinePoint(){
        if(this.loc == PointLoc.LINE)
            return true;
        else
            return false;
    }
    public boolean isMethodEnter(){
        if(this.loc == PointLoc.ENTER)
            return true;
        else 
            return false;
    }
    public boolean isMethodExit(){
        if(this.loc == PointLoc.EXIT)
            return true;
        else 
            return false;
    }
    public boolean isOther(){
        if(this.loc == PointLoc.OTHER)
            return true;
        else
            return false;
    }
    @Override
    public String toString(){
        return this.packageName+"."+this.className;
    }
}
