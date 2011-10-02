package others;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import utils.MathUtil;

public class TestRandom {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//testGetRandomInteger();
		testGetUniqueRandomIntSet();
	}

	
	private static void testGetRandomInteger(){
		Random random = new Random();
		
		for (int i=0;i<10;i++){
			int r = MathUtil.getRandomInteger(1,10,random);
			System.out.println("i="+i+", r="+r);
		}
	}

	
	private static void testGetUniqueRandomIntSet(){
		Random random = new Random();
		
		List<Integer> resultList = MathUtil.getUniqueRandomIntSet(1000,4600,random,10);
		System.out.println(resultList.toString());
		
//		for(Integer i : resultList){
//			System.out.pr
//		}
	}
	
	


}
