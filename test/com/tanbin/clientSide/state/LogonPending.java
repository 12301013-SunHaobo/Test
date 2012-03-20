//$Id$
package com.tanbin.clientSide.state;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.tanbin.bothSide.MyConst;
import com.tanbin.bothSide.format.FIXFormatter;
import com.tanbin.bothSide.format.FIXMsgField;


public class LogonPending extends AbstractSate{
	@Override
	public Map<?, String> getNextMsgToServer(int seq) {
		Map<FIXMsgField,String> m = 
			new LinkedHashMap<FIXMsgField,String>(); 
		m.put(FIXMsgField.MsgType, "A");//logon
		m.put(FIXMsgField.MsgSeqNum, seq + "");
		m.put(FIXMsgField.SenderCompID, MyConst.sender);
		m.put(FIXMsgField.TargetCompID, MyConst.target);
		return m;
//		String ret = FIXFormatter.mapToFIXString(m);
//		
//		return ret;
	}

}
