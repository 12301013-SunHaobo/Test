package others;

import java.util.Collection;

public class TestLoopNullCollection {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Collection<String> c = null;
        for(String s : c){
            System.out.println(s);
        }

    }

}
