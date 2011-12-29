package others.apachemath;

import org.apache.commons.math.stat.correlation.Covariance;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math.stat.regression.SimpleRegression;

import utils.Formatter;

public class TestApacheMath {

    /**
     * @param args
     */
    public static void main(String[] args) {

        testSimpleRegression1();
    	//testSimpleRegression2();
    	//testCovariance();
    }

    /**
     * 1 线性相关
     * -1 反向线性相关
     * 0 不相关
     */
    private static void testCovariance(){
    	double[] x = new double[]{1,2,3,4,5,6};
    	
    	double[] y = new double[]{1,0,3,4,5,6};
    	double[] z = new double[]{1,0,-1,-2,-3,-4};
    	
    	Covariance c = new Covariance();
    	System.out.println("covariance(x,y)="+c.covariance(x, y));
    	System.out.println("covariance(x,z)="+c.covariance(x, z));
    	
    	PearsonsCorrelation p = new PearsonsCorrelation();
    	System.out.println("correlation(x,y)="+p.correlation(x, y));
    	System.out.println("correlation(x,z)="+p.correlation(x, z));
    }
    
    private static void testSimpleRegression1(){
        SimpleRegression r = new SimpleRegression();
        r.addData(0d, 0d);
        r.addData(3d, 1d);
        System.out.println("predict="+Formatter.DECIMAL_FORMAT.format(r.predict(5)));
        System.out.println("slope="+Formatter.DECIMAL_FORMAT.format(r.getSlope()));
        r.clear();
        r.addData(0d, 0d);
        r.addData(6d, 1d);
        System.out.println("slope="+Formatter.DECIMAL_FORMAT.format(r.getSlope()));
        
    }

    private static void testSimpleRegression2() {
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

}
