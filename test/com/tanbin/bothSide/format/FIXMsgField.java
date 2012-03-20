//$Id$
package com.tanbin.bothSide.format;

public enum FIXMsgField {
	ClOrdID(11), HandInst(
			21), MsgSeqNum(34), MsgType(35), OrderQty(38), OrdType(40), SenderCompID(49), Side(
			54), Symbol(55), TargetCompID(56), TimeInForce(59), TransactTime(60);

	public final int tag;

	private FIXMsgField(int i) {
		this.tag = i;
	}

	@Override
	public String toString() {
		return this.tag + "";
	}
}
