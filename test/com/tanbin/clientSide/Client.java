//$Id$
package com.tanbin.clientSide;

import java.io.*;
import java.net.*;
import java.util.Map;

import com.tanbin.bothSide.MyConst;
import com.tanbin.bothSide.MyUtil;
import com.tanbin.bothSide.format.FIXMsgField;
import com.tanbin.bothSide.format.FIXParser;
import com.tanbin.clientSide.ConfigServiceFactory.ConfigType;
import com.tanbin.clientSide.state.AbstractSate;
import com.tanbin.clientSide.state.LogoutPending;
import com.tanbin.clientSide.state.LogonPending;
import com.tanbin.clientSide.state.NewOrderPending;

/**
 * one instance represents one connection/session to the server.
 *
 */
public class Client extends AbstractClient implements MyConst {
	private final IConfigService configService;
	final String serverHostName;
	final Socket clientSocket;
	final PrintWriter out;
	final BufferedReader in;
	final int clientId;
	private ClientSession session;

	public Client(int id, ConfigType configType) {
		this.clientId = id;
		configService = ConfigServiceFactory.getConfigService(configType);
		serverHostName = configService.getServerHostName();
		try {
			clientSocket = new Socket(serverHostName, 4444);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket
					.getInputStream()));
		} catch (UnknownHostException e) {
			throw new RuntimeException(String.format(
				"host unknown: %s. Irrecoverable error.", serverHostName), e);
		} catch (IOException e) {
			throw new RuntimeException(
					String
							.format(
								"Couldn't get I/O for the connection to: %s. Irrecoverable error.",
								serverHostName), e);
		}
	}

	@Override
	protected void carryConversation() throws IOException {
		AbstractSate state = new LogonPending();
		state.send(out, 1);
		String fromServer = null;

		while ((fromServer = in.readLine()) != null) {
			System.out.println("Server: " + fromServer);
			Map<?, String> msgMap = FIXParser.parse(fromServer);
			//System.out.println(m);

			if (session != null) {
				int seqReceived;
				try {
					seqReceived = Integer.parseInt(msgMap.get(FIXMsgField.MsgSeqNum
							.toString()));
				} catch (NumberFormatException e) {
					throw new IllegalStateException(
							"Serious system error. FIX server should never send a non-integer under tag 34.");
				}
				int expectedIncomingSeq = session.getExpectedIncomingSeq();
				System.out.printf("%s, %s\n", expectedIncomingSeq,seqReceived);
				if (seqReceived == expectedIncomingSeq) {
					System.out.println("seq matched");
					session.rollExpectedIncomingSeq();
				} else if (seqReceived > expectedIncomingSeq) {
					System.out
							.println("Seq too high. Should send Resend Request <2> message");
				} else {
					System.out.println("Seq too low. Should disconnect");
				}
			}

			if (fromServer.contains(SOH + "35=A" + SOH)) {
				// rudimentary detection for logon ack

				//TODO should spawn new thread to handle heartbeat
				
				this.session = new ClientSession();
				state = new NewOrderPending();
			} else if (fromServer.contains(SOH + "35=8" + SOH)) {
				// rudimentary detection for ORDER ack
				state = new LogoutPending();
			} else if (fromServer.contains(SOH + "35=5" + SOH)) {
				// rudimentary detection for logout ack

				// should interrupt heatbeat thread
				break;
			} else {// something we don't care or know how to process
				// TODO: check timer. If too long, then send warning email

			}
			int seqNum = session == null ? 1 : session.incrementAndGetSeq();
			state.send(out, seqNum);
		}
	}

	/**
	 * release all resources
	 * @throws IOException 
	 */
	@Override
	public void close() {
		this.session.close();
		this.session = null;
		out.close();
		try {
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			// i don't think this error is a showstopper. Just log and proceed.
			e.printStackTrace();
		}
	}
}
