package demo.app;

public class PhD extends Student{
    static int ttttttttttttt = 1111;
    private int grade;
    private Teacher tutor;
    
    public PhD(int ID, String name, int grade, Teacher tutor){
        super(ID,name);
        this.grade = grade;
        this.tutor = tutor;
    }
    
    public void print(){
        super.print();
        System.out.println(this.grade);
        this.tutor.print();
    }
}
