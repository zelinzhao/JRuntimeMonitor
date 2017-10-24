package demo.app;

public class Main {

    public static void createPhD() {
        Teacher teac = new Teacher(30, "ABC", "CS");
        PhD phd = new PhD(1, "0000001", 4, teac);
        phd.print();
        phd = new PhD(2, "0000002", 4, teac);
        phd.print();
        phd = new PhD(3, "0000003", 4, teac);
        phd.print();
        phd = new PhD(4, "0000004", 4, teac);
        phd.print();
    }

    public static void main(String[] args) {
        createPhD();
        
    }
}