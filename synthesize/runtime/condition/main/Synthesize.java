package synthesize.runtime.condition.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.cli.Options;

import collect.runtime.information.condition.Condition;
import collect.runtime.information.main.ProgramPoint;
import read.runtime.condition.tool.ReadConditions;

public class Synthesize {
    static Options ops = new Options();
    static HashMap<ProgramPoint, HashSet<Condition>> positiveConds = new HashMap<ProgramPoint, HashSet<Condition>> ();
    static HashMap<ProgramPoint, HashSet<Condition>> negativeConds = new HashMap<ProgramPoint, HashSet<Condition>> ();
    
    /**
     * Remove same conditions from positive and negative conditions.
     * Positive conditions and negative conditions should be mutual exclusion.
     */
    private static void cleanIntersection(){
        for(ProgramPoint poPP : positiveConds.keySet()){
            HashSet<Condition> poConds = positiveConds.get(poPP);
            HashSet<Condition> neConds = negativeConds.get(poPP);
            if(poConds == null || neConds == null )
                return;
            
            Iterator<Condition> iter = poConds.iterator();
            while(iter.hasNext()){
                Condition temp = iter.next();
                if(neConds.contains(temp)){
                    iter.remove();
                    neConds.remove(temp);
                }
            }
        }
    }
    
    public static void main(String [] args) throws IOException{
        positiveConds = ReadConditions.readConditions("E:\\Eclipse\\posi.txt");
        negativeConds = ReadConditions.readConditions("E:\\Eclipse\\nega.txt");
        cleanIntersection();
        //TODO: Need to relax the positive and negative conditions
        
        positiveConds.forEach((key,value) -> {
            System.out.println(key.toString());
            value.forEach((element) -> {
                System.out.println("\t" + element.toString());
            });
        });
        negativeConds.forEach((key,value) -> {
            System.out.println(key.toString());
            value.forEach((element) -> {
                System.out.println("\t" + element.toString());
            });
        });
    }
}
