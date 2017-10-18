package collect.runtime.information.main;

enum PointLoc{
    ENTER, EXIT, LINE, OTHER;
}

public abstract class ProgramPoint {
    protected String fullClassName="";
    protected PointLoc loc = PointLoc.OTHER;
    
    public ProgramPoint(String fullClassName, PointLoc loc){
        this.fullClassName = fullClassName;
        this.loc = loc;
    }
    
    public String getFullClassName(){
        return this.fullClassName;
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
        return this.fullClassName;
    }
    
    @Override
    public boolean equals(Object obj){
        if(obj==null)
            return false;
        if( !(obj instanceof ProgramPoint))
            return false;
        ProgramPoint pp = (ProgramPoint)obj;
        
        if(!pp.fullClassName.equals(this.fullClassName) || pp.loc!=this.loc)
            return false;
        return true;
    }
    
    public boolean equals(String fullClassName){
        return this.fullClassName.equals(fullClassName);
    }
}
