package others;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

public class TestCommonsMath {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		test1();
	}

	private static void test1(){
		DescriptiveStatistics ds = new DescriptiveStatistics();
		ds.setWindowSize(5);
		ds.addValue(1);
		ds.addValue(2);
		ds.addValue(3);
		ds.addValue(4);
		ds.addValue(5);
		ds.addValue(6);
		ds.addValue(7);
		
		System.out.println("getN():"+ds.getN());
		System.out.println("getSum():"+ds.getSum());
		
	}
	
}
