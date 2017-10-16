package collect.runtime.information.main;

public class LinePoint extends ProgramPoint{
    private int lineNo;
    public LinePoint(String packageName, String className) {
        super(packageName, className, PointLoc.LINE);
    }
    
    public LinePoint(String packageName, String className, int lineNo){
        this(packageName, className);
        this.lineNo = lineNo;
    }
    
    public LinePoint(String point){
        super(null,null,PointLoc.LINE);
        this.packageName = point.substring(0, point.lastIndexOf('.'));
        this.className = point.substring(point.lastIndexOf('.')+1, point.indexOf(':'));
        this.lineNo = Integer.valueOf(point.substring(point.indexOf(':')+1));
    }
    
    public void setLineNo(int lineNo){
        this.lineNo = lineNo;
    }
    
    public int getLineNo(){
        return this.lineNo;
    }
    
    @Override
    public String toString(){
        return super.toString()+":"+this.lineNo;
    }

}
