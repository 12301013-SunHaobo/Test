//$Id$
package com.tanbin.clientSide;

import java.io.Closeable;
import java.io.IOException;

abstract public class AbstractClient implements Closeable {

	/**
	 * template method to ensure we always close()
	 */
	final public void sendNewOrderSingle() throws IOException {
		try {
			this.carryConversation();
		} finally {
			this.close();
		}
	}

	abstract protected void carryConversation() throws IOException;
}
