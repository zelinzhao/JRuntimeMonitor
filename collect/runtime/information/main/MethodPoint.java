package collect.runtime.information.main;


public class MethodPoint extends ProgramPoint{
    private String methodName = "";
    private String methodDesc = "";
    /**
     * While setting method entry/exit point in program, line no is precise.
     * Here, use lineNo to identify a method point more precisely.
     */
    private int lineNo ;
    
    public MethodPoint(String fullClassName, String methodName, String methodDesc, PointLoc loc){
        super(fullClassName, loc);
        this.methodName = methodName;
        this.methodDesc = methodDesc;
    }
    public MethodPoint(String point){
        super(null, PointLoc.OTHER);
        //package.class-method-descriptor:enter
        //package.class-method-descriptor:exit
        this.fullClassName = point.substring(0,point.indexOf('-'));
        this.methodName = point.substring(point.indexOf('-')+1, point.lastIndexOf('-'));
        this.methodDesc = point.substring(point.lastIndexOf('-')+1, point.indexOf(':'));
        if( point.substring(point.indexOf(':')+1).equals("enter") )
            this.loc = PointLoc.ENTER;
        else if ( point.substring(point.indexOf(':')+1).equals("exit"))
            this.loc = PointLoc.EXIT;
        else
            System.out.println(point +" may be a wrong point");
    }
    public void setLineNo(int lineNo){
        this.lineNo = lineNo;
    }
    
    public String getMethodName(){
        return this.methodName;
    }
    public String getMethodDesc(){
        return this.methodDesc;
    }
    
    @Override
    public String toString(){
        if(this.loc == PointLoc.ENTER)
            return super.toString()+"-"+this.methodName+"-"+this.methodDesc+":enter";
        else
            return super.toString()+"-"+this.methodName+"-"+this.methodDesc+":exit";
    }
    
    @Override
    public boolean equals(Object obj){
        if(!super.equals(obj))
            return false;
        if( !(obj instanceof MethodPoint))
            return false;
        MethodPoint mp = (MethodPoint) obj;
        if(!mp.methodName.equals(this.methodName) || !mp.methodDesc.equals(this.methodDesc) || mp.lineNo!= this.lineNo)
            return false;
        return true;
    }
    
    public boolean equals(String className, String methodName, String methodDesc, int lineNo){
        if( !super.equals(className) )
            return false;
        if(!this.methodName.equals(methodName) || !this.methodDesc.equals(methodDesc) || this.lineNo!= lineNo)
            return false;
        return true;
    }
}
