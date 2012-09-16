package others.iv;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This program add 1 to 1 billion dollars by 1 increment.
 * <p>
 * Executor with fixed pool size is used for multi-threading implementation.
 * 
 * @author Roland Dong - Thanks to Xiao Rong and Tan Bin for their inspiration.
 * 
 */
public class AddTo1BillionDong {
	private static final long theNum = 1000000000;
	// Created this many threads. As size of the pool increases,
	// the performance may increase.
	private static final int NO_OF_THREAD = 501;

	public static void main(String args[]) throws Exception {
		addOneBillionUsingExecutor(addOnBillionResultSet());
		addOneBillionUsingLoop(theNum);
	}

	/**
	 * Inner class implements Callable and performs multi-threading tasks. Each
	 * thread will be assigned a block of numbers by Excutor and adds the
	 * numbers from the first one of the block to the last one of the block
	 * incrementally
	 */
	public static class AddNumCallable implements Callable {
		private long start = 0;
		private long end = 0;

		public AddNumCallable(long start, long end) {
			// System.out.println("start is " + start + " end is " + end);
			this.start = start;
			this.end = end;
		}

		@Override
		public BigInteger call() {
			long temp = 0;
			for (long i = start; i <= end; i++) {
				temp += i;
			}
			return BigInteger.valueOf(temp);
		}
	}

	/**
	 * A for loop solution for adding up from 1 to 1 billion.
	 * 
	 * @param bigN
	 * @return Used to compare the result from multi-threading solution
	 */
	private static long addOneBillionUsingLoop(long bigN) {

		long beforeTime = System.nanoTime();
		long sum2 = 0;

		for (long i = 0; i <= bigN; i++) {
			sum2 += i;
		}
		long afterTime = System.nanoTime();
		long timeDiff = afterTime - beforeTime;
		System.out.println("Add up to a billion using for loop is " + sum2 + " and it takes this much time: " + getSeconds(timeDiff)
				+ " second(s)");
		return sum2;
	}

	/**
	 * Executor solution for adding from 1 to 1 billion.
	 */
	private static void addOneBillionUsingExecutor(Set<Future<BigInteger>> set) throws Exception {
		long beforeTime = System.nanoTime();
		BigInteger sum = BigInteger.valueOf(0);

		for (Future<BigInteger> future : set) {
			sum = sum.add(future.get());
		}

		long afterTime = System.nanoTime();
		long timeDiff = afterTime - beforeTime;
		System.out.println("Number of threads used: " + set.size());
		System.out.println("Add up to a billion using Exeutor  is " + sum + " and it takes this much time: " + getSeconds(timeDiff)
				+ " second(s)");
	}

	/**
	 * Each thread will calculate the result and store it into a set.
	 * 
	 * @return set
	 */
	private static Set<Future<BigInteger>> addOnBillionResultSet() {
		long beforeTime = System.nanoTime();
		long remainder = theNum % NO_OF_THREAD;
		long wholeNum = theNum - remainder;
		long batch = theNum / NO_OF_THREAD;

		ExecutorService pool = Executors.newFixedThreadPool(NO_OF_THREAD);
		Set<Future<BigInteger>> set = new HashSet<Future<BigInteger>>();

		long start = 1;
		long end = batch;
		while (end <= theNum) {
			if (remainder > 0 && end == wholeNum) {
				end = wholeNum + remainder;
			}
			Callable<BigInteger> callable = new AddNumCallable(start, end);
			start += batch;
			end += batch;
			Future<BigInteger> future = pool.submit(callable);
			set.add(future);
		}
		pool.shutdown();
		return set;
	}

	private static double getSeconds(long nanosec) {
		return (double) nanosec / 1000000000.0;
	}

}
