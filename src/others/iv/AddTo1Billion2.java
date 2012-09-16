package others.iv;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import utils.BoundedExecutor;

/**
 * 
 * calculate the sum of numbers starting from minNumber to maxNumber in
 * multithreading process, each thread calculate a batchSize of numbers
 * 
 * e.g sum from 1 to 1000,000,005 = 500000005500000015
 */
public class AddTo1Billion2 {

	private static final long batchSize = 1000 * 1000;
	private static final long maxNumber = 1000 * 1000 * 1000 + 5;
	private static final long minNumber = 1;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testCalculation();
	}

	private static void testCalculation() {
		ArrayList<Future<Long>> results = new ArrayList<Future<Long>>();

		BoundedExecutor be = new BoundedExecutor(10);

		long i = minNumber;

		// i+batchSize-1<=maxNumber
		while (i+batchSize-1<=maxNumber) {
			// System.out.println("start="+i+"  end="+(i+batchSize-1));
			addBatchTask(be, results, i, i+batchSize-1);
			i+=batchSize;
		}
		// i<=maxNumber
		if (i<=maxNumber) {
			// System.out.println("start="+i+"  end="+maxNumber);
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

	private static void addBatchTask(BoundedExecutor be, ArrayList<Future<Long>> results, 
			long start, long end) {
		Callable<Long> batchCalculator = new BatchCalculator(start, end);
		Future<Long> task = null;
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
	private static class BatchCalculator implements Callable<Long> {
		private long start;
		private long end;

		public BatchCalculator(long start, long end) {
			super();
			this.start = start;
			this.end = end;
		}

		@Override
		public Long call() throws Exception {
			long total = 0;
			for (long i = this.start; i <= this.end; i++) {
				total = total +i;
			}
			System.out.println("bc:start=" + this.start + ":end=" + this.end);
			return total;
		}
	}

	private static void printResults(ArrayList<Future<Long>> results) {
		long total = 0;
		int i = 0;
		for (Future<Long> result : results) {
			try {
				long sum = result.get();
				System.out.println("idx=" + i + " sum=" + sum + " total=" + total);
				total = total + result.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			i++;
		}
		System.out.println("sum from " + minNumber + " to " + maxNumber + " = " + total);

	}

}
