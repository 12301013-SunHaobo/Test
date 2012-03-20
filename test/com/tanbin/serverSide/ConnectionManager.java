//$Id$
package com.tanbin.serverSide;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionManager {
	ServerSocket serverSocket = null;

	public ConnectionManager() { // no need to throw
		try {
			serverSocket = new ServerSocket(4444);
		} catch (IOException e) {
			System.err.println("Could not listen on port: 4444.");
			System.exit(1);
		}
	}

	public Socket accept() {
		Socket clientSocket = null;
		try {
			clientSocket = serverSocket.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return clientSocket;
	}

	public void close() {
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException("error closing server socket", e);
		}
	}
}
