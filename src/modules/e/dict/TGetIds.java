package others.e.dict;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.http.client.HttpClient;

public class TGetIds implements Callable<String> {

	private static List<String> idsList = Collections.synchronizedList(new ArrayList<String>());
	HttpClient httpclient;
	int pageNum = 0;
	
	public TGetIds(HttpClient httpclient, int pageNum) {
		super();
		this.httpclient = httpclient;
		this.pageNum = pageNum;
	}




	@Override
	public String call() throws Exception {

		String content = DictUtil.getContent(httpclient, DictUtil.PAGE_URL+(this.pageNum-1)*20);
		List<String> ids = DictUtil.getIds(content);
		TGetIds.idsList.addAll(ids);
		
		return null;
	}




	public static List<String> getIdsList() {
		return idsList;
	}
	
	

}