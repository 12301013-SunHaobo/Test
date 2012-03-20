//$Id$
package com.tanbin.bothSide.format;

import java.util.HashMap;
import java.util.Map;

import com.tanbin.bothSide.MyConst;

public class FIXParser implements MyConst{

	public static Map<?, String> parse(String fromServer) {
		Map<? super Object , String> ret = new HashMap<Object, String>();
		
		String[] pairs = fromServer.split(SOH);
		for (String pair: pairs) {
			//System.out.println(pair);
			String[] tmp = pair.split("=");
			if (tmp.length != 2) { //for this test, server can send invalid format
				continue; 
			}
			String tag = tmp[0];
			
			ret.put(tag, tmp[1]);
		}
		return ret;
	}

}
