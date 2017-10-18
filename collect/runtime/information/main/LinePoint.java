package collect.runtime.information.main;

public class LinePoint extends ProgramPoint{
    private int lineNo;

    public LinePoint(String fullClassName, int lineNo){
        super(fullClassName, PointLoc.LINE);
        this.lineNo = lineNo;
    }
    
    public LinePoint(String point){
        super(null,PointLoc.LINE);
        this.fullClassName = point.substring(0,point.indexOf(':'));
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
    
    @Override
    public boolean equals(Object obj){
        if( !super.equals(obj))
            return false;
        if( !(obj instanceof LinePoint))
            return false;
        LinePoint lp = (LinePoint)obj;
        if(lp.lineNo != this.lineNo)
            return false;
        return true;
    }
    public boolean equals(String className, int lineNo){
        if( !super.equals(className) )
            return false;
        if( this.lineNo != lineNo)
            return false;
        return true;
    }

}
