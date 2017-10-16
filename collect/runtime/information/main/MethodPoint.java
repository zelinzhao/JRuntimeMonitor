package collect.runtime.information.main;


public class MethodPoint extends ProgramPoint{
    private String methodName = "";
    private String methodDesc = "";
    
    public MethodPoint(String packageName, String className, PointLoc loc) {
        super(packageName, className, loc);
    }
    public MethodPoint(String packageName, String className, String methodName, String methodDesc, PointLoc loc){
        this(packageName, className, loc);
        this.methodName = methodName;
        this.methodDesc = methodDesc;
    }
    public MethodPoint(String point){
        super(null,null, PointLoc.OTHER);
        //package.class-method-descriptor:enter
        //package.class-method-descriptor:exit
        this.packageName = point.substring(0, point.lastIndexOf('.'));
        this.className = point.substring(point.lastIndexOf('.')+1, point.indexOf('-'));
        this.methodName = point.substring(point.indexOf('-')+1, point.lastIndexOf('-'));
        this.methodDesc = point.substring(point.lastIndexOf('-')+1, point.indexOf(':'));
        if( point.substring(point.indexOf(':')+1).equals("enter") )
            this.loc = PointLoc.ENTER;
        else if ( point.substring(point.indexOf(':')+1).equals("exit"))
            this.loc = PointLoc.EXIT;
        else
            System.out.println(point +" may be a wrong point");
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
}
