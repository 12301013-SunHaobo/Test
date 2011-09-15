package others.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BoundedExecutor {
    private final ExecutorService executorService;
    private final Semaphore semaphore;

    public BoundedExecutor(ExecutorService executorService, int bound) {
        this.executorService = executorService;
        this.semaphore = new Semaphore(bound);
    }

    public Future<String> submit(final Callable<String> command) throws InterruptedException {
        semaphore.acquire();
        Future<String> f = null;
        try {
            f = executorService.submit(new Callable<String>() {
                public String call() {
                    String s = null;
                    try {
                        s = command.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        semaphore.release();
                    }
                    return s;
                }
            });

        } catch (RejectedExecutionException e) {
            semaphore.release();
            throw e;
        }
        return f;
    }
    
    
    //simplified construction
    public BoundedExecutor(int bound) {
    	int poolSize = bound;
    	int maxPoolSize = poolSize;
    	long keepAliveTime = 1;
    	final LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
    	ThreadPoolExecutor tpe = new ThreadPoolExecutor(poolSize, maxPoolSize,
    			keepAliveTime, TimeUnit.DAYS, queue);
    	
        this.executorService = tpe;
        this.semaphore = new Semaphore(bound);
    }

	public ExecutorService getExecutorService() {
		return executorService;
	}


    
    
    
}
