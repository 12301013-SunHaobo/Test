//$Id$
package com.tanbin.bothSide;

public class MyUtil {
	public static boolean hasText(String s) {
		boolean noText = s == null || s.isEmpty();
		return ! noText;
	}
}
