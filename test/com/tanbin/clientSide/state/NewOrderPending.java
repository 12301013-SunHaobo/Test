//$Id$
package com.tanbin.clientSide.state;

import java.util.LinkedHashMap;
import java.util.Map;

import com.tanbin.bothSide.MyConst;
import com.tanbin.bothSide.format.FIXFormatter;
import com.tanbin.bothSide.format.FIXMsgField;


public class NewOrderPending extends AbstractSate {

	@Override
	public Map<?, String> getNextMsgToServer(int seq) {
		Map<FIXMsgField,String> m = 
			new LinkedHashMap<FIXMsgField,String>(); 
		m.put(FIXMsgField.MsgType, "D");
		m.put(FIXMsgField.MsgSeqNum, seq + "");
		m.put(FIXMsgField.SenderCompID, MyConst.sender);
		m.put(FIXMsgField.TargetCompID, MyConst.target);
		m.put(FIXMsgField.OrderQty, "1000");
		m.put(FIXMsgField.TimeInForce, "1");
		m.put(FIXMsgField.OrdType, "1");
		m.put(FIXMsgField.ClOrdID, "ORD10001");
		m.put(FIXMsgField.Symbol, "HPQ");
		m.put(FIXMsgField.Side, "1");
		m.put(FIXMsgField.HandInst, "2");
		m.put(FIXMsgField.TransactTime, FIXFormatter.getCurrentTime());
		
		// other fields omitted....
		return m;
	}
}
