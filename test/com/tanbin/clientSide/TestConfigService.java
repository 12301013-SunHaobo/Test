//$Id$
package com.tanbin.clientSide;

public class TestConfigService implements IConfigService {
	String serverHostName;

	/**
	 * 
	 set up configurations useing the config file;
	 * 
	 * @param configFile
	 */
	public TestConfigService(String configFile) {
		this.serverHostName = "localhost";
	}

	public TestConfigService() {
		this("TestConfig.txt, but ignored in this test");
	}

	@Override
	public String getServerHostName() {
		return this.serverHostName;
	}
}
