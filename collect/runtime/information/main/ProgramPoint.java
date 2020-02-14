package collect.runtime.information.main;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

enum PointLoc {
    ENTER, EXIT, LINE, OTHER;
}

enum TimesType {
    BEFORE, AFTER, AT;

    /** Used for BEFORE or AFTER. Default is 0. */
    int times = 0;
    /** Used for AT. Use concurrent queue. */
    ConcurrentLinkedQueue<Integer> timesQueue;
}

public class ProgramPoint {
    private String fullClassName;
    private PointLoc loc = PointLoc.OTHER;

    private TimesType timeType = TimesType.AFTER;

    private AtomicInteger nowTimes = new AtomicInteger(0);
    /** only for AT points */
    private AtomicInteger preTimes = new AtomicInteger(0);

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
     * For line number point, this field is line number; For method entry point,
     * this field is the first location of this method; For method exit point,
     * this field is the last location of this method;
     */
    private int lineNo;

    public ProgramPoint() {
    }

    public ProgramPoint(String point) {
        if (!point.contains("@"))
            point = point + "@";

        if (point.contains("-")) {
            this.fullClassName = point.substring(0, point.indexOf('-'));
            this.methodName = point.substring(point.indexOf('-') + 1, point.lastIndexOf('-'));
            this.methodDesc = point.substring(point.lastIndexOf('-') + 1, point.indexOf(':'));
            if (point.substring(point.indexOf(':') + 1, point.indexOf('@')).equals("enter"))
                this.loc = PointLoc.ENTER;
            else if (point.substring(point.indexOf(':') + 1, point.indexOf('@')).equals("exit"))
                this.loc = PointLoc.EXIT;
            else
                System.out.println(point + " may be a wrong point");
        } else {
            this.fullClassName = point.substring(0, point.indexOf(':'));
            this.lineNo = Integer.valueOf(point.substring(point.indexOf(':') + 1, point.indexOf('@')));
            this.loc = PointLoc.LINE;
        }
        String times = point.substring(point.indexOf('@') + 1);
        String[] sp = times.split("@");
        if (sp.length > 0)
            switch (sp[0]) {
                case "AT": {
                    this.timeType = TimesType.AT;
                    this.timeType.timesQueue = new ConcurrentLinkedQueue<Integer>();
                    for (int i = 1; i < sp.length; i++)
                        this.timeType.timesQueue.add(Integer.valueOf(sp[i]));
                    this.nowTimes.set(this.timeType.timesQueue.poll());
                    break;
                }
                case "BEFORE": {
                    this.timeType = TimesType.BEFORE;
                    this.timeType.times = Integer.valueOf(sp[1]);
                    if (this.timeType.times <= 1)
                        System.exit(-1);
                    this.nowTimes.set(1);
                    break;
                }
                case "AFTER": {
                    this.timeType = TimesType.AFTER;
                    this.timeType.times = Integer.valueOf(sp[1]);
                    if (this.timeType.times <= 0)
                        System.exit(-1);
                    this.nowTimes.set(this.timeType.times);
                    break;
                }
                default: {
                    break;
                }
            }
    }

    /**
     * should call this function BEFORE {@link #needReset()} and
     * {@link #nextTimes()}
     */
    public void incrementTimes() {
        if (this.timeType == TimesType.BEFORE || this.timeType == TimesType.AFTER)
            this.nowTimes.incrementAndGet();
        if (this.timeType == TimesType.AT)
            if (this.timeType.timesQueue.isEmpty())
                return;
            else {
                this.preTimes.set(this.nowTimes.get());
                this.nowTimes.set(this.timeType.timesQueue.peek());
            }
    }

    public int initialTimes() {
        if (this.timeType == TimesType.BEFORE && this.nowTimes.get() == 1)
            return 0;
        return this.nowTimes.get();
    }

    /**
     * should call this function AFTER {@link #incrementTimes()}, BEFORE
     * {@link #nextTimes()}
     * 
     * @return
     */
    public boolean needReset() {
        if (this.timeType == TimesType.BEFORE)
            if (this.nowTimes.get() < this.timeType.times)
                return false;
            else
                return true;
        if (this.timeType == TimesType.AFTER)
            if (this.nowTimes.get() == this.timeType.times + 1)
                return true;
            else
                return false;
        if (this.timeType == TimesType.AT)
            if (this.timeType.timesQueue.isEmpty())
                return false;
            else
                return true;
        return true;
    }

    /**
     * 
     * @return positive numbers, next stop times; 0, reset to forever stop;
     *         negative, disable this point
     */
    public int nextTimes() {
        if (this.timeType == TimesType.BEFORE && this.nowTimes.get() >= this.timeType.times)
            return -1;
        else if (this.timeType == TimesType.AFTER && this.nowTimes.get() == this.timeType.times + 1)
            return 0;
        else if (this.timeType == TimesType.AT)
            if (this.timeType.timesQueue.isEmpty())
                return -1;
            else
                return (this.timeType.timesQueue.poll() - this.preTimes.get());
        return -1;
    }

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
            return "@@," + this.fullClassName + "-" + this.methodName + "-" + this.methodDesc + ":enter" + "@AT@"
                    + this.nowTimes.get();
        else if (this.loc == PointLoc.EXIT)
            return "@@," + this.fullClassName + "-" + this.methodName + "-" + this.methodDesc + ":exit" + "@AT@"
                    + this.nowTimes.get();
        else if (this.loc == PointLoc.LINE)
            return "@@," + this.fullClassName + ":" + this.lineNo + "@AT@" + this.nowTimes.get();
        else
            return null;
    }

    /**
     * Comparing to another program point, which {@code PointLoc} should be
     * clear. This method does Not compare the line number if they are method
     * point, because when reading point and conditions from file, the line
     * number is not known.
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
    public int hashCode() {
        switch (loc) {
            case ENTER: {
                return (this.fullClassName + this.methodName + this.methodDesc + this.loc).hashCode();
            }
            case EXIT: {
                return (this.fullClassName + this.methodName + this.methodDesc + this.loc).hashCode();
            }
            case LINE: {
                return (this.fullClassName + this.lineNo + this.loc).hashCode();
            }
            case OTHER: {
                return this.loc.hashCode();
            }
            default: {
                return 0;
            }
        }
    }

    /**
     * 
     * @param fullClassName
     * @param methodName
     * @param methodDesc
     * @param lineNo
     *            must be provided. If not clear, just give a negative number.
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
        if (lineNo >= 0)
            if (this.lineNo != lineNo)
                return false;
            else
                return true;
        return true;
    }
}
