package others.iv.enumsingleton;

import java.lang.reflect.Method;

public class TestHackEnumSingleton {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Method methods[] = ESingleton.class.getDeclaredMethods();//"ESingleton", argClasses);
		System.out.println();
//		method.setAccessible(true);
//		return method.invoke(targetObject, argObjects);


	}

}
