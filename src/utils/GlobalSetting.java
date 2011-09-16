package utils;

public class GlobalSetting {

	public static final String TEST_HOME = getTestHome();

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
