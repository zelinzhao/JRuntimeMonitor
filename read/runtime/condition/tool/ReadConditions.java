package read.runtime.condition.tool;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import collect.runtime.information.condition.Condition;
import collect.runtime.information.condition.ElementNumberCondition;
import collect.runtime.information.condition.FieldValueCondition;
import collect.runtime.information.condition.MethodExistCondition;
import collect.runtime.information.condition.ObjectNumberCondition;
import collect.runtime.information.main.ProgramPoint;

public class ReadConditions {

    private String inputFile = "E:\\Eclipse\\conditions.txt";
    private BufferedReader reader ;

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
        try {
            this.reader = new BufferedReader(new FileReader(this.inputFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void readConditions() throws IOException {
        HashMap<ProgramPoint, List<Condition>> pointConditions = new HashMap<ProgramPoint, List<Condition>>();
        
        ProgramPoint currentPoint =null;
        List<Condition> currentConditions = new ArrayList<Condition>();
        
        String temp = reader.readLine();
        String get = "";
        while (temp != null) {
            System.out.println("read: "+temp);
            //clear all whitespace
            String[] sp = temp.replaceAll("\\s", "").split(",");
            // sp[0] defines a stop point or a condition
            switch (sp[0]) {
                case "@@":{ // it is a stop point
                    ProgramPoint pp = new ProgramPoint(sp[1]);
                    System.out.println("get:  "+ pp.toString());
                    //put previous point and conditions in the map
                    if(currentPoint!=null)
                        pointConditions.put(currentPoint, currentConditions);
                    //re-initialized point and conditions for this new point
                    currentPoint = pp;
                    currentConditions.clear();
                    break;
                }
                default:{ // it is a condition
                    switch (sp[1]){ //different conditions
                        case ElementNumberCondition.type:{
                            ElementNumberCondition enc = new ElementNumberCondition(currentPoint, temp);
                            currentConditions.add(enc);
                            System.out.println("get:  " + enc.toString());
                            get = enc.toString();
                            break;
                        }
                        case FieldValueCondition.type:{
                            FieldValueCondition fvc = new FieldValueCondition(currentPoint,temp);
                            currentConditions.add(fvc);
                            System.out.println("get:  " + fvc.toString());
                            get = fvc.toString();
                            break;
                        }
                        case MethodExistCondition.type:{
                            MethodExistCondition mec = new MethodExistCondition(currentPoint, temp);
                            currentConditions.add(mec);
                            System.out.println("get:  " + mec.toString());
                            get = mec.toString();
                            break;
                        }
                        case ObjectNumberCondition.type:{
                            ObjectNumberCondition onc = new ObjectNumberCondition(currentPoint, temp);
                            currentConditions.add(onc);
                            System.out.println("get:  " + onc.toString());
                            get = onc.toString();
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
    }
    public static void main(String[] args){
        try {
            ReadConditions rc = new ReadConditions();
            rc.reader = new BufferedReader(new FileReader(rc.inputFile));
            rc.readConditions();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
