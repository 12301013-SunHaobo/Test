package others.iv;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import utils.BoundedExecutor;

/**
 * e.g sum from 1 to 1000,000,005 = 500000005500000015
 * 
 * calculate the sum of numbers starting from minNumber to maxNumber
 * in multithreading process, each thread calculate a batchSize of numbers  
 */
public class AddTo1Billion {

	private static final BigInteger batchSize = BigInteger.valueOf(1000*1000);
	private static final BigInteger maxNumber = BigInteger.valueOf(1000*1000*1000+5);
	private static final BigInteger minNumber = BigInteger.valueOf(1);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testOneBatch();
	}

	private static void testOneBatch(){
		ArrayList<Future<BigInteger>> results = new ArrayList<Future<BigInteger>>();
		
		BoundedExecutor be = new BoundedExecutor(10);
		
		BigInteger i=minNumber;
		
		//i+batchSize-1<=maxNumber
		BigInteger tmpEnd = i.add(batchSize).subtract(BigInteger.valueOf(1));
		BigInteger compareResult = tmpEnd.subtract(maxNumber);
		while(compareResult.signum()<=0){
			//System.out.println("start="+i+"  end="+(i+batchSize-1));
			addBatchTask(be, results, i, tmpEnd);
			//i+=batchSize;
			i = i.add(batchSize);
			tmpEnd = i.add(batchSize).subtract(BigInteger.valueOf(1));
			compareResult = tmpEnd.subtract(maxNumber);
		}
		//i<=maxNumber
		compareResult = i.subtract(maxNumber);
		if(compareResult.signum()<=0){
			//System.out.println("start="+i+"  end="+maxNumber);
			addBatchTask(be, results, i, maxNumber);
		}
		be.getExecutorService().shutdown();
		try {
			be.getExecutorService().awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		printResults(results);
		
	}
	
	private static void addBatchTask(BoundedExecutor be, ArrayList<Future<BigInteger>> results, BigInteger start, BigInteger end){
		Callable<BigInteger> batchCalculator = new BatchCalculator(start, end);
		Future<BigInteger> task = null;
		try {
			task = be.submit(batchCalculator);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		results.add(task);
	}
	
	/**
	 * Calculator to sum up one batch of numbers 
	 */
	private static class BatchCalculator implements Callable<BigInteger> {
		private BigInteger start;
		private BigInteger end;
		
		public BatchCalculator(BigInteger start, BigInteger end) {
			super();
			this.start = start;
			this.end = end;
		}

		@Override
		public BigInteger call() throws Exception {
			BigInteger total = BigInteger.valueOf(0);
			for(int i=this.start.intValue(); i<=this.end.intValue(); i++){
				total = total.add(BigInteger.valueOf(i));
			}
			System.out.println("bc:start="+this.start+":end="+this.end);
			return total;
		}
	}

	private static void printResults(ArrayList<Future<BigInteger>> results) {
		BigInteger total = BigInteger.valueOf(0);
		int i = 0;
		for(Future<BigInteger> result : results){
			try {
				BigInteger sum = result.get();
				System.out.println("idx="+i+" sum="+sum+" total="+total);
				total = total.add(result.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			i++;
		}
		System.out.println("sum from "+minNumber+" to "+maxNumber+" = "+ total);
		
	}
	
	
}
