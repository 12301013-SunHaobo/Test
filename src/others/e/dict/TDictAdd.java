package others.e.dict;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class TDictAdd implements Callable<String> {
    
    private final HttpClient httpClient;
    private final String name;
    private final int idx;
    
    public TDictAdd(HttpClient httpClient, String name, int idx) {
        this.httpClient = httpClient;
        this.name = name;
        this.idx = idx;
    }
    
    @Override
    public String call() {
        try {
        	DictUtil.addWord(this.httpClient, this.name);
        	System.out.println(this.idx+":done");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    
   
}
