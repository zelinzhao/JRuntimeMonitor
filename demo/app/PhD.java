package demo.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PhD extends Student{
    String str = "str";
    Boolean Boo = true;
    boolean boo = false;
    Byte Byt = 0;
    byte byt = 2;
    Character Charac = 'a';
    char cha = 'b';
    Double Dou = 2.333;
    double dou = 4.444;
    Float Flo = 5.555f;
    float flo = 6.666f;
    Integer Inte = 1000;
    int in = 123;
    Long Lon = 345556l;
    long lon = 3465l;
    Short Sho = 10;
    short sho = 445;
    
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
