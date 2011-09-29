package utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GlobalSetting {

    public static final int MOCK_SERVER_PORT = 8900;
    public static final String MOCK_SERVER_IP = getMockServerIp();
    
	public static final String TEST_HOME = getTestHome();

    
    private static String getMockServerIp(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }
    
	private static String getTestHome() {
		if ("r".equals(System.getProperty("user.name", "not set"))) {
			return "D:/projects/java/workspace/Test";
		} else if ("rxia".equals(System.getProperty("user.name"))) {
			return "D:/user/workspaces/pk12/Test";
		} else {
			System.err.println("Not office or home computer. Please check GlobalSetting.java");
			System.exit(-1);
			return null;
		}
	}

	public static void main(String args[]) {
		System.out.println(TEST_HOME);
	}
}
