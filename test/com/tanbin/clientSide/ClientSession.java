//$Id$
package com.tanbin.clientSide;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * main job is to control seq num.
 * 
 * One instance for each sequence number stream
 *
 */
public class ClientSession implements Closeable {
	private AtomicInteger seqOut = new AtomicInteger(1);
	private AtomicInteger seqInExpected = new AtomicInteger(2);//logon ack skipped
	public int incrementAndGetSeq() {
		int ret = this.seqOut.incrementAndGet();
		return ret;
	}
	public int getExpectedIncomingSeq() {
		return seqInExpected.get();
	}
	public void rollExpectedIncomingSeq() {
		seqInExpected.incrementAndGet();
	}
	@Override
	public void close(){
		// object should be garbaged collected soon
		seqOut = seqInExpected = null; // any accidental use after close would throw NPE
	}	
}
