//package others;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Date;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Properties;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
//import org.apache.commons.lang.time.StopWatch;
//
//import PredatorSrv.ServerException;
//
//import com.ml.Predator2.ReportServer.ReportException;
//import com.ml.liberty.ctd.feed.DirtyPricer;
//import com.ml.liberty.ctd.util.BoundedExecutor;
//import com.ml.liberty.ctd.util.CTDUtil;
//import com.ml.liberty.ctd.util.StageUtil;
//
//public class TestDirtyPricer {
//
//	private static List<String[]> algoList = new ArrayList<String[]>();
//	private static List<String[]> issuerList = new ArrayList<String[]>();
//	/**
//	 * @param args
//	 * @throws Exception 
//	 * @throws ReportException 
//	 * @throws ServerException 
//	 */
//	public static void main(String[] args) throws Exception {
//		StopWatch sw =new StopWatch();
//		sw.start();
//		
//		long b0 = sw.getTime();
//
//		testBoundedExecutor(args);
//		//testDirtyPricer();
//		//testBlockingQueue();
//		long e0 = sw.getTime();
//		
//		System.out.println("used time:"+(e0-b0));
//		
//	}
//	
//	
//	
//	private static void testBoundedExecutor(String[] args) throws Exception {
//		//init ThreadPoolExecutor
//		int poolSize = 40;
//		int maxPoolSize = 50;
//		long keepAliveTime = 3;
//		final LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
//		ThreadPoolExecutor tpe = new ThreadPoolExecutor(poolSize, maxPoolSize, keepAliveTime, TimeUnit.MINUTES, queue);
//		BoundedExecutor be = new BoundedExecutor(tpe, maxPoolSize);
//		
//		
//		//init issuerList
//		load();
//		
//		List<String> resultList = Collections.synchronizedList(new LinkedList<String>()); 
//		List<String> errorList = Collections.synchronizedList(new LinkedList<String>()); 
//		DirtyPricer.init(issuerList, resultList, errorList, args);
//		
//		StopWatch sw =new StopWatch();
//		sw.start();
//		long b2 = sw.getTime();
//		//submit tasks
//		for (int i = 0; i < algoList.size() ; i++) { //algoList.size()
//			
//			DirtyPricer dp = new DirtyPricer("Thread-"+i,algoList.get(i));
//			try {
//				be.submit(dp);
//				System.out.println(i+":submited:");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		tpe.shutdown();
//		try {
//			tpe.awaitTermination(1,TimeUnit.DAYS);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		long e2 = sw.getTime();
//		System.out.println("calculate time:"+(e2-b2));
//		
//		//save result
//		Properties properties = CTDUtil.getProperties(System.getProperty("FEED_FILE"));
//		String RESULT_DIR = properties.getProperty("DIRTY_RPICE_RESULT_DIR");
//		DateFormat dfLog = new SimpleDateFormat("yyyyMMdd-hhmmss");
//		try {
//			StageUtil.listToFile(resultList, RESULT_DIR+"/m-result-"+dfLog.format(new Date())+".txt");
//			StageUtil.listToFile(errorList, RESULT_DIR+"/m-error-"+dfLog.format(new Date())+".txt");
//			
//			//saveToStageDirtyPrice(resultList);
//		} catch (Exception e) {
//			System.err.println("Error: " + e.getMessage());
//		}
//		
//	}
//	
//
//	private static void load() throws IOException {
//
//		Properties properties = CTDUtil.getProperties(System.getProperty("FEED_FILE"));
//		String ISSUER_FILE_PATH = properties.getProperty("ISSUER_FILE");
//		
//		List<String> isins = new ArrayList<String>();
//		String algoChargedAssetsFile = StageUtil.getAlgoChargedAssetsFileName();
//		BufferedReader input = new BufferedReader(new FileReader(algoChargedAssetsFile));
//		String line = null;
//		String[] lineArr = null;
//		while ((line = input.readLine()) != null) {
//			lineArr = line.split("\\|", -1);
//
//			if(!"".equals(lineArr[28].trim())){//chargedAssetId
//				if(!"".equals(lineArr[29].trim())){
//					isins.add(lineArr[29]);//isin in algoList
//					algoList.add(lineArr);
//				}
//			}
//		}
//		input.close();
//		algoList.remove(0);
//		System.out.println("ALGO file loaded. algoList.size():"+algoList.size());
//
//		StopWatch sw = new StopWatch();
//		sw.start();
//		long b1 = sw.getTime();
//		issuerList = StageUtil.issuerFileToList(ISSUER_FILE_PATH, isins);
//		long e1 = sw.getTime();
//		System.out.println("Issuer file loaded. used time:"+(e1-b1));
//		
//	}	
//	
//	
//	private static void testDirtyPricer() {
//
//		int poolSize = 2;
//		int maxPoolSize = 5;
//		long keepAliveTime = 3;
//		final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(5);
//		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(poolSize, maxPoolSize, keepAliveTime, TimeUnit.MINUTES, queue);
//
//		for (int i = 0; i < 20; i++) {
////			DirtyPricer dp = new DirtyPricer("DP" + i);
////			threadPool.submit(dp);
//		}
//		threadPool.shutdown();
//	}
//
//	
//	private static void testBlockingQueue(){
//		ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(5);
//		
//		for(int i=0;i<10;i++){
//			queue.add("item"+i);
//		}
//	}
//	
//}
