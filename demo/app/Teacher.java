package demo.app;

public class Teacher {
    private int ID;
    private String name;
    private String major;
    
    public Teacher(int ID, String name, String major){
        this.ID = ID;
        this.name = name;
        this.major = major;
    }
    
    public void print(){
        System.out.print("teacher: "+this.ID+" "+ this.name+" "+ this.major);
    }
}
