package others.e.dict;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.StopWatch;
import org.apache.http.client.HttpClient;

import others.utils.BoundedExecutor;
import others.utils.FileUtil;

public class TaskDict {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception { 

		StopWatch sw = new StopWatch();
		sw.start();
		long b0 = sw.getTime();
		HttpClient httpclient = DictUtil.login();

		addAll(httpclient);
		//deleteAll(httpclient);
		
        httpclient.getConnectionManager().shutdown();        
        long e0 = sw.getTime();
        System.out.println("Total used time:"+(e0-b0));
		
	}

	private static void deleteAll(HttpClient httpclient)throws Exception{
		//collect all ids
		String scbContent = DictUtil.getContent(httpclient, DictUtil.SCB_URL);
		int remainingPage = DictUtil.pageCount(scbContent);
		System.out.println("reaminingPage:"+remainingPage);

		BoundedExecutor be = new BoundedExecutor(100);
		for (int i=1;i<=remainingPage;i++) {
			try {
				be.submit(new TGetIds(httpclient, i));
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
		
		List<String> idList = TGetIds.getIdsList();
		System.out.println("idsList.size():"+idList.size());
		
		//delete all ids
		DictUtil.delete(httpclient, idList);
		System.out.println("deleteAll():done");
		
		
	}
	
	private static void addAll(HttpClient httpclient)throws Exception{
		List<String> wList = FileUtil.fileToList(DictUtil.INPUT_FILE);

		BoundedExecutor be = new BoundedExecutor(100);
		for (int i = 0; i < wList.size(); i++) {
			String httpget = wList.get(i);

			try {
				be.submit(new TDictAdd(httpclient, httpget, i));
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
	
	
}
