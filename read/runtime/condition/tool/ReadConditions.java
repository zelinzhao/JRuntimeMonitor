package read.runtime.condition.tool;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import collect.runtime.information.condition.Condition;
import collect.runtime.information.condition.ElementNumberCondition;
import collect.runtime.information.condition.FieldValueCondition;
import collect.runtime.information.condition.MethodExistCondition;
import collect.runtime.information.condition.ObjectNumberCondition;
import collect.runtime.information.main.ProgramPoint;

public class ReadConditions {

    public static HashMap<ProgramPoint, HashSet<Condition>> readConditions(String inputFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        HashMap<ProgramPoint, HashSet<Condition>> pointConditions = new HashMap<ProgramPoint, HashSet<Condition>>();
        
        ProgramPoint currentPoint =null;
        HashSet<Condition> currentConditions = new HashSet<Condition>();
        
        String temp = reader.readLine();
        while (temp != null) {
            //clear all whitespace
            String[] sp = temp.replaceAll("\\s", "").split(",");
            // sp[0] defines a stop point or a condition
            switch (sp[0]) {
                case "@@":{ // it is a stop point
                    ProgramPoint pp = new ProgramPoint(sp[1]);
                    //put previous point and conditions in the map
                    if(currentPoint!=null && !currentConditions.isEmpty())
                        pointConditions.put(currentPoint, currentConditions);
                    //re-initialized point and conditions for this new point
                    currentPoint = pp;
                    currentConditions = pointConditions.get(currentPoint);
                    if(currentConditions == null)
                        currentConditions = new HashSet<Condition>();
                    break;
                }
                default:{ // it is a condition
                    switch (sp[1]){ //different conditions
                        case ElementNumberCondition.type:{
                            ElementNumberCondition enc = new ElementNumberCondition(currentPoint, temp);
                            currentConditions.add(enc);
                            break;
                        }
                        case FieldValueCondition.type:{
                            FieldValueCondition fvc = new FieldValueCondition(currentPoint,temp);
                            currentConditions.add(fvc);
                            break;
                        }
                        case MethodExistCondition.type:{
                            MethodExistCondition mec = new MethodExistCondition(currentPoint, temp);
                            currentConditions.add(mec);
                            break;
                        }
                        case ObjectNumberCondition.type:{
                            ObjectNumberCondition onc = new ObjectNumberCondition(currentPoint, temp);
                            currentConditions.add(onc);
                            break;
                        }
                        default:{
                            System.out.println("Unrecognized condition: "+ temp);
                            break;
                        }
                    }
                    break;
                }
            }
            temp = reader.readLine();
        }
        reader.close();

        if(!currentConditions.isEmpty())
            pointConditions.put(currentPoint, currentConditions);
        return pointConditions;
    }
//    public static void main(String[] args){
//        try {
//            ReadConditions rc = new ReadConditions();
//            rc.reader = new BufferedReader(new FileReader(rc.inputFile));
//            rc.readConditions();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
}
