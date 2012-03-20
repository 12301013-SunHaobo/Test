//$Id$
package com.tanbin.clientSide;

import java.io.*;
import java.net.*;

import com.tanbin.clientSide.ConfigServiceFactory.ConfigType;

public class ClientMain {
	public static void main(String[] args) throws IOException {
		int fakeClientId = 123;
		AbstractClient sw = new Client(fakeClientId, ConfigType.TEST);
		sw.sendNewOrderSingle();
	}
}
