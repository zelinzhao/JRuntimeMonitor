package demo.app;

public class Main {
    
    public static void createPhD(){
        Teacher teac = new Teacher(30, "ABC", "CS");
        PhD phd = new PhD(20,"ZZL",4, teac);
        phd.print();
    }
    public static void main(String[] args){
        System.out.println("Here is main method");
        PhD nullTutor = new PhD(10,"z",10,null);
        PhD nullPhd = null;
        createPhD();
    }
}
