package napi;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;

public class TestNapiUtil {

    private static Map<String, String> oibUidToPwdMap = new HashMap<String, String>();
    static {
        oibUidToPwdMap.put("mtacha72784", "Demo1234");
        oibUidToPwdMap.put("amarch", "Demo1234");
        oibUidToPwdMap.put("hmister", "1234");
        oibUidToPwdMap.put("rking", "Demo1234");
    }
    
    
    /**
     * For NapiClientTest.java
     * @throws Exception
     */
    public static String getMasToken(String oibUid) throws Exception {
        System.setProperty("HTTPClient.cookies.save","true");
        
        HttpClient client = new HttpClient();

        client.getState().setCredentials(
                new AuthScope("mclasshome-local.wgenhq.net:8001", 443, "realm"),
                new UsernamePasswordCredentials(oibUid, oibUidToPwdMap.get(oibUid)));
        
        PostMethod get = new PostMethod("https://mclasshome-local.wgenhq.net:8001/wgen/Login.do");
        int status = client.executeMethod(get);
        System.out.println("wgen/login.do:status:"+status);
        
        get = new PostMethod("https://mclasshome-local.wgenhq.net:8001/wgen/Authenticate.do");
        get.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
        get.setDoAuthentication(true);
        NameValuePair[] data = { 
                new NameValuePair("username", oibUid), 
                new NameValuePair("password", oibUidToPwdMap.get(oibUid)),
                new NameValuePair("account", ""), 
                new NameValuePair("redirect", ""), 
                new NameValuePair("numTries", "") };
        get.setRequestBody(data);
        // execute the GET
        status = client.executeMethod(get);
        // print the status and response
        System.out.println("wgen/Authenticate.do:"+status);
        
        Cookie[] cookies = client.getState().getCookies();
        String token = getToken(cookies);
        System.out.println(token);
        
        // release any connection resources used by the method
        get.releaseConnection();
        return token;
    }    
    
    private static String getToken(Cookie[] cookies){
        if ((cookies != null) && (cookies.length > 0)) {
            for (int i = 0; i < cookies.length; ++i) {
                if(cookies[i].getName().equals("sso.auth_token")) {
                    return cookies[i].getValue();
                }
            }
        }
        return null;
    }    
    
    
    
    
    
    
    
    
    
    
    /**
     * For 
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        String possibleToken = (String)request.getAttribute("sso.auth_token");
        javax.servlet.http.Cookie possibleCookie = null;
        if(possibleToken == null) {
            // then look for a cookie
            possibleCookie = getCookieFromRequest(request,"sso.auth_token");
            if(possibleCookie != null) {
                possibleToken = possibleCookie.getValue();
            }
        }
        System.out.println("token:["+possibleToken+"]");
        return possibleToken;
    }
    private static javax.servlet.http.Cookie getCookieFromRequest(HttpServletRequest request, String cookieName) {
        javax.servlet.http.Cookie[] cookies = request.getCookies();
        if ((cookies != null) && (cookies.length > 0)) {
            for (int i = 0; i < cookies.length; ++i) {
                if(cookies[i].getName().equals(cookieName)) {
                    return cookies[i];
                }
            }
        }
        return null;
    }  
    
    
    
}
