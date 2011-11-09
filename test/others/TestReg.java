package others;

public class TestReg {

    /**
     * @param args
     */
    public static void main(String[] args) {
        //$1 means replace with captured subsequences
        System.out.println("src=\"http://100".replaceAll(
                "src=\"http://([0-9]+)\"",
                "src=\"/oib/image\\?imageId=$1\""
                ));

    }

}
