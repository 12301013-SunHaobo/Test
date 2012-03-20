//$Id$
package com.tanbin.serverSide;

import java.net.*;
import java.io.*;

import com.tanbin.bothSide.KnockKnockProtocol;

public class ServerMain {
	static ConnectionManager cm = new ConnectionManager();

	public static void main(String[] args) throws IOException {
		handleClient(new ClientProxy(cm.accept()));
		cm.close();
	}

	private static void handleClient(ClientProxy p) throws IOException {
		if (p == null)
			return;
		String inputLine, outputLine;
		KnockKnockProtocol kkp = new KnockKnockProtocol();
//
//		outputLine = kkp.processInput(null);
//		p.out.println(outputLine);

		while ((inputLine = p.in.readLine()) != null) {
			outputLine = kkp.processInput(inputLine);
			p.out.println(outputLine);
			if (outputLine.equals("logout"))
				break;
		}
		p.close();
	}
}
