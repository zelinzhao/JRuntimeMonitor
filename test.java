import java.util.ArrayList;
import java.util.List;

enum TEnum{
    A,B,C
}
public class test {

    public static void test(List<String> t){
        t.add("aa");
    }
    public static void main(String[]args){
//        String point = "package1.package2.class-method-descriptor:enter";
//        System.out.println(point.substring(0, point.lastIndexOf('.')));
//        System.out.println(point.substring(point.lastIndexOf('.')+1, point.indexOf('-')));
//        System.out.println(point.substring(point.indexOf('-')+1, point.lastIndexOf('-')));
//        System.out.println(point.substring(point.lastIndexOf('-')+1, point.indexOf(':')));
//        System.out.println(point.substring(point.indexOf(':')+1));
//        TEnum t = TEnum.A;
//        System.out.println(t);
//        System.out.println(t.toString());
//        System.out.println(String.valueOf(t));
//        
//        List<String> h = new ArrayList<String>();
//        h.add("b");
//        test(h);
//        System.out.println(h);
        
        String t = "==,ELEMENT, package.class , the.field.path , the.field.type, 0/1/2/3";
        
        String[] sp = t.split(",");
        for(String str: sp)
            System.out.println("|"+str.trim()+"|");
        
    }
}
