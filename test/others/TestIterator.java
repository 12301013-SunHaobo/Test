package others;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestIterator {

    /**
     * @param args
     */
    public static void main(String[] args) {

        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        
        Iterator<String> it = list.iterator();
        while(it.hasNext()){
            System.out.println(it.next());
        }
        System.out.println("------------------ 1");
        
        Iterator<String> it2 = list.iterator();
        while(it2.hasNext()){
            System.out.println(it2.next());
        }
        
    }

}
