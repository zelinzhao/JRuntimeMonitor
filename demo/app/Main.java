package demo.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    List<PhD> phds = new ArrayList<PhD>();
    Set<Object> objs = new HashSet<Object>();
    Map<String, Object> maps = new HashMap<String, Object>();
    Object[] arrays = { "adf", 10, new Student(44, "stu44") };
    public Main(){
        phds.add(null);
        phds.add(new PhD(22, "a", 3, null));

        objs.add("abcd");
        objs.add(12);
        objs.add(new Student(100, "stu100"));

        maps.put("a", new Student(23, "stu23"));
        maps.put("h", new PhD(30, "30", 30, null));
    }

    public static void createPhD() {
        Teacher teac = new Teacher(30, "ABC", "CS");
        PhD phd = new PhD(20, "ZZL", 4, teac);
        Main m = new Main();
        phd.print();
    }

    public static void main(String[] args) {
        PhD nullTutor = new PhD(10, "z", 10, null);
        PhD nullPhd = null;
        createPhD();
    }
}
