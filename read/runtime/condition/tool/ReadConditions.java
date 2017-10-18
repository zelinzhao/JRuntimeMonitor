package read.runtime.condition.tool;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import collect.runtime.information.condition.ElementNumberCondition;
import collect.runtime.information.condition.FieldValueCondition;
import collect.runtime.information.condition.MethodExistCondition;
import collect.runtime.information.condition.ObjectNumberCondition;
import collect.runtime.information.main.ProgramPoint;

public class ReadConditions {

    private String inputFile;
    private BufferedReader reader;

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
        try {
            this.reader = new BufferedReader(new FileReader(this.inputFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void readConditions() throws IOException {
        String temp = reader.readLine();
        while (temp != null) {
            //clear all whitespace
            String[] sp = temp.replaceAll("\\s", "").split(",");
            // sp[0] defines a stop point or a condition
            switch (sp[0]) {
                case "@@":{ // it is a stop point
                    ProgramPoint pp = new ProgramPoint(sp[1]);
                    break;
                }
                default:{ // it is a condition
                }
            }
            temp = reader.readLine();
        }
        reader.close();
    }

}
