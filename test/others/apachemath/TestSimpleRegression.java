package others.apachemath;

import org.apache.commons.math.stat.regression.SimpleRegression;

public class TestSimpleRegression {

    /**
     * @param args
     */
    public static void main(String[] args) {

        //test1();
        test2();

    }

    private static void test2() {
        SimpleRegression r = new SimpleRegression();
        r.addData(1d, 10d);
        r.addData(2d, 11d);
        r.addData(3d, 9d);
        r.addData(4d, 8d);
        r.addData(5d, 9d);
        r.addData(6d, 8d);
        double predict = r.predict(7);
        System.out.println(predict);
        
    }
    
    private static void test1(){
        SimpleRegression r = new SimpleRegression();
        r.addData(1d, 0d);
        r.addData(3d, 1d);
        double predict = r.predict(5);
        System.out.println(predict);
    }

}
