package demo.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PhD extends Student{
    private Teacher tutor;
    
    int grade;
    Integer integer;
    
    public PhD(int ID, String name, int grade, Teacher tutor){
        super(ID,name);
        this.grade = grade;
        this.tutor = tutor;
        this.integer = 100;
    }
    
    public void print()
    
    {
        
        super.print();
        
        System.out.println(this.grade);
        
        this.tutor.print();  
        }
}