package demo.app;

public class Student {
    private int ID;
    private String name;
    
    public Student(int ID, String name){
        this.ID = ID;
        this.name = name;
    }
    public void setID(int ID){
        this.ID = ID;
    }
    public int getID(){
        return this.ID;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    
    public void print(){
        System.out.println(this.ID +" "+ this.name);
    }
}
