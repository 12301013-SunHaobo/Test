package others;

import java.io.PrintStream;

public class TestSystemOut {

    //Not very clear about what this is doing
    //Listener that prints failures so maven cannot hide them from you.
    public static void main(String[] args) throws Exception {
        ((PrintStream) System.class.getDeclaredField("out").get(null)).println("test output");
    }

}
