//$Id$
package com.tanbin.bothSide.format;

import java.text.SimpleDateFormat;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import com.tanbin.bothSide.MyConst;

public class FIXFormatter {
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyyMMdd-HH:mm:ss");

	public static void main(String[] args) {
		// System.out.printf("%03d", 11);
		HashMap<?, String> msg = getTestMsgMap();
		String fixString = mapToFIXString(msg);
		System.out.println(fixString);
	}

	public static String getCurrentTime() {
		return DATE_FORMAT.format(new java.util.Date());
	}

	public static String mapToFIXString(Map<?, String> msgMap) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<?, String> entry : msgMap.entrySet()) {
			Object key = entry.getKey();
			String tag = FIXMsgField.class.equals(key.getClass()) ? key
					.toString() : MyConst.FIX_DICT.get(key);
			sb.append(tag);
			sb.append("=");
			sb.append(entry.getValue());
			sb.append(MyConst.SOH);
		}
		sb.append("52=");
		sb.append(getCurrentTime());
		sb.append(MyConst.SOH);
		sb.append("108=30"); // hardcode heartbeat interval
		sb.append(MyConst.SOH);
		sb.append("98=0"); // hardcode encryption method
		sb.append(MyConst.SOH);

		sb.insert(0, "8=FIX.4.2" + MyConst.SOH + "9=" + sb.length() + MyConst.SOH);
		return appendCheckSum(sb.toString());
	}

	private static String appendCheckSum(String s) {
		int checkSum = 0;
		for (char aChar : s.toCharArray()) {
			checkSum += (int) aChar;
		}
		return String.format(s + "10=%03d" + MyConst.SOH, checkSum % 256);
	}

	private static HashMap<Object, String> getTestMsgMap() {
		HashMap<Object, String> msgHM = new HashMap<Object, String>();
		msgHM.put(FIXMsgField.SenderCompID, MyConst.sender);
		msgHM.put("Account", "12345678");
		// msgHM.put("AccruedInterestAmt", "100.89");
		// msgHM.put("AccruedInterestRate", "0.02");
		// msgHM.put("AdvId", "999");
		// msgHM.put("AdvRefID", "888");

		return msgHM;
	}
}