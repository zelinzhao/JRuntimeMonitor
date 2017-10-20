package collect.runtime.information.main;

enum PointLoc {
    ENTER, EXIT, LINE, OTHER;
}

public class ProgramPoint {
    private String fullClassName;
    private PointLoc loc = PointLoc.OTHER;

    /**
     * for method entry/exit point, used with {@link methodDesc}. {@link loc}
     * should be {@code PointLoc.ENTER} or {@code PointLoc.EXIT}
     */
    private String methodName;
    /**
     * for method entry/exit point, used with {@link methodName}. {@link loc}
     * should be {@code PointLoc.ENTER} or {@code PointLoc.EXIT}
     */
    private String methodDesc;

    /**
     * For line number point, this field is line number;
     * For method entry point, this field is the first location of this method;
     * For method exit point, this field is the last location of this method;
     */
    private int lineNo;

    public ProgramPoint() {
    }

    public ProgramPoint(String point) {
        if (point.contains("-")) {
            this.fullClassName = point.substring(0, point.indexOf('-'));
            this.methodName = point.substring(point.indexOf('-') + 1, point.lastIndexOf('-'));
            this.methodDesc = point.substring(point.lastIndexOf('-') + 1, point.indexOf(':'));
            if (point.substring(point.indexOf(':') + 1).equals("enter"))
                this.loc = PointLoc.ENTER;
            else if (point.substring(point.indexOf(':') + 1).equals("exit"))
                this.loc = PointLoc.EXIT;
            else
                System.out.println(point + " may be a wrong point");
        } else {
            this.fullClassName = point.substring(0, point.indexOf(':'));
            this.lineNo = Integer.valueOf(point.substring(point.indexOf(':') + 1));
            this.loc = PointLoc.LINE;
        }
    }

    // public void setFullClassName(String
    // className){this.fullClassName=className;}
    public String getFullClassName() {
        return this.fullClassName;
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    public int getLineNo() {
        return this.lineNo;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public String getMethodDesc() {
        return this.methodDesc;
    }

    public boolean isLinePoint() {
        if (this.loc == PointLoc.LINE)
            return true;
        else
            return false;
    }

    public boolean isMethodEnter() {
        if (this.loc == PointLoc.ENTER)
            return true;
        else
            return false;
    }

    public boolean isMethodExit() {
        if (this.loc == PointLoc.EXIT)
            return true;
        else
            return false;
    }

    public boolean isOther() {
        if (this.loc == PointLoc.OTHER)
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        if (this.loc == PointLoc.ENTER)
            return "@@,"+this.fullClassName + "-" + this.methodName + "-" + this.methodDesc + ":enter";
        else if (this.loc == PointLoc.EXIT)
            return "@@,"+this.fullClassName + "-" + this.methodName + "-" + this.methodDesc + ":exit";
        else if (this.loc == PointLoc.LINE)
            return "@@,"+this.fullClassName + ":" + this.lineNo;
        else
            return null;

    }

    /**
     * Comparing to another program point, which {@code PointLoc} should be clear.
     * This method does Not compare the line number if they are method point, because 
     * when reading point and conditions from file, the line number is not known.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof ProgramPoint))
            return false;
        ProgramPoint pp = (ProgramPoint) obj;
        if (!this.fullClassName.equals(pp.fullClassName)) // same class
            return false;
        if (this.loc != pp.loc) // same type
            return false;
        if (this.loc == PointLoc.LINE)// both line point
            if (this.lineNo != pp.lineNo)
                return false;
            else
                return true;
        // now, both method point
        if (!this.methodName.equals(pp.methodName) || !this.methodDesc.equals(pp.methodDesc))
            return false;
        return true;
    }
    @Override
    public int hashCode(){
        switch (loc){
            case ENTER:{
                return (this.fullClassName+this.methodName+this.methodDesc+this.loc).hashCode();
            }case EXIT:{
                return (this.fullClassName+this.methodName+this.methodDesc+this.loc).hashCode();
            }case LINE:{
                return (this.fullClassName+this.lineNo+this.loc).hashCode();
            }case OTHER:{
                return this.loc.hashCode();
            }default:{
                return 0;
            }
        }
    }

    /**
     * 
     * @param fullClassName
     * @param methodName
     * @param methodDesc
     * @param lineNo must be provided. If not clear, just give a negative number.
     * @return
     */
    public boolean equals(String fullClassName, String methodName, String methodDesc, int lineNo) {
        if (!this.fullClassName.equals(fullClassName))
            return false;
        if (this.loc == PointLoc.LINE)
            if (this.lineNo != lineNo)
                return false;
            else
                return true;
        if (!this.methodName.equals(methodName) || !this.methodDesc.equals(methodDesc))
            return false;
        if (lineNo >=0)
            if(this.lineNo != lineNo)
                return false;
            else
                return true;
        return true;
    }
}
