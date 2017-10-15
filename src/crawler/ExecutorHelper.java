package crawler;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorHelper {
	private static  ThreadPoolExecutor executor;
	
	static {
		executor = null;
	}
	
	public static ThreadPoolExecutor getInstance(){
		
		if (executor == null) {
			executor = new ThreadPoolExecutor(50, 100,60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		}
		
		return executor;
	}
}
