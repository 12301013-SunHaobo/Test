package others;

import java.math.BigInteger;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestThreadPoolExecutor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

    	int poolSize = 5;
    	int maxPoolSize = 10;
    	long keepAliveTime = 1;
    	final LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(10);
    	ThreadPoolExecutor tpe = new ThreadPoolExecutor(poolSize, maxPoolSize,
    			keepAliveTime, TimeUnit.DAYS, queue);

		for(int i=0;i<100;i++){
			Callable<BigInteger> batchCalculator = new BatchCalculator(i);
			Future<BigInteger> task = null;
			task = tpe.submit(batchCalculator);

		}
		tpe.shutdown();
		try {
			tpe.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Calculator to sum up one batch of numbers
	 */
	private static class BatchCalculator implements Callable<BigInteger> {
		private int start;

		public BatchCalculator(int start) {
			super();
			this.start = start;
		}

		@Override
		public BigInteger call() throws Exception {
			System.out.println("bc:start=" + this.start );
			return null;
		}
	}	

}
