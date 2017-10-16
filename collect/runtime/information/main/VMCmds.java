package collect.runtime.information.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class VMCmds{
    /**
     * full class name
     */
    private HashMap<String, List<ProgramPoint>> stopCmds = new HashMap<String, List<ProgramPoint>>();

    public VMCmds(HashMap<String, List<ProgramPoint>> stopCmds){
        this.stopCmds = stopCmds;
    }
    
    public VMCmds() {
        // TODO Auto-generated constructor stub
    }

    public void addCmd(ProgramPoint pp){
        String className = pp.getFullClassName();
        List<ProgramPoint> lpp = this.stopCmds.get(className);
        if(lpp == null){
            lpp = new ArrayList<ProgramPoint>();
        }
        lpp.add(pp);
        this.stopCmds.put(className, lpp);
    }
    
    public Set<String> getStopClass() {
        return this.stopCmds.keySet();
    }
    
    public boolean hasStopClass(String stopClass){
        return this.stopCmds.keySet().contains(stopClass);
    }

    public HashMap<String, List<ProgramPoint>> getAllStopCmds() {
        return this.stopCmds;
    }

    public List<ProgramPoint> getStopCmdsByClass(String classname) {
        return this.stopCmds.get(classname);
    }
    
    @Override
    public String toString(){
        String result = "";
        for(String str: stopCmds.keySet()){
            for(ProgramPoint pp: stopCmds.get(str))
                result += pp.toString() + "\n";
        }
        return result;
    }

}