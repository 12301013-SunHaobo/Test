//$Id$
package com.tanbin.clientSide.state;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import com.tanbin.bothSide.format.FIXFormatter;
import com.tanbin.clientSide.ClientSession;

public abstract class AbstractSate {
	final long started= new Date().getTime();; // stateTransitionStart
	private volatile boolean isSent = false;

	public abstract Map<?, String> getNextMsgToServer(int seq);

	synchronized public void send(PrintWriter connToServer, int seq) {
		if (isSent) {
			// it's dangerous to resend a New Order Single.
			// Exchange may treat it as another order!
			return;
		}
		String toServer = FIXFormatter.mapToFIXString(getNextMsgToServer(seq));
		System.out.println("Client: " + toServer);
		connToServer.println(toServer);
		this.isSent = true;
	}
}
