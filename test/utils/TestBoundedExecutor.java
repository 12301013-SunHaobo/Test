package utils;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class TestBoundedExecutor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		BoundedExecutor be = new BoundedExecutor(10);
		for (int i = 0; i < 10; i++) {
			OneTask tp = new OneTask("t"+i);
			try {
				be.submit(tp);
				System.out.println(i + ":submited:");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		be.getExecutorService().shutdown();
		try {
			be.getExecutorService().awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}

	
	private static class OneTask implements Callable<String>{
		private String name;
		
		public OneTask(String name) {
			super();
			this.name = name;
		}

		@Override
		public String call() throws Exception {
			System.out.println(this.name+"is done.");
			return null;
		}
	}
}
