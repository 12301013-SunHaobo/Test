//$Id$
package com.tanbin.bothSide;

import java.net.*;
import java.io.*;

public class KnockKnockProtocol implements MyConst {
	private static final int WAITING = 0;
	private static final int SENTKNOCKKNOCK = 1;
	private static final int LOGON_ACK_SENT = 2;
	private static final int ORDER_ACK_SENT = 3;

	private static final int LOGOUT_ACK_SENT = 99;

	private int state = WAITING;

	public String processInput(String theInput) {
		String theOutput = null;

		if (state == WAITING) {
			// theOutput = "";
			// state = SENTKNOCKKNOCK;
			// } else if (state == SENTKNOCKKNOCK) {
			if (theInput.contains(MyConst.SOH + "35=A" + SOH)) {// logon
				theOutput = "8=FIX.4.2" + SOH + "35=A" + SOH + "34=1" + SOH
						+ " ....";
				state = LOGON_ACK_SENT;
			} else {
				theOutput = "please logon....";
			}
		} else if (state == LOGON_ACK_SENT) {
			if (theInput.contains(MyConst.SOH + "35=D" + SOH)) {// New order
				// single
				theOutput = "8=FIX.4.2" + SOH + "35=8" + SOH + "34=2" + SOH
						+ " ...."; // 8 = Order Ack
				state = ORDER_ACK_SENT;
			} else {
				theOutput = "Only New order single supported....";
			}
		} else if (state == ORDER_ACK_SENT) {
			if (theInput.contains(MyConst.SOH + "35=5" + SOH)) {// logout
				theOutput = "8=FIX.4.2" + SOH + "35=5" + SOH + "34=3" + SOH
						+ " ....";
				state = LOGOUT_ACK_SENT;

				// Now clean up
			} else {
				theOutput = theInput + " received. State unchanged";
			}
		}
		return theOutput;
	}
}
