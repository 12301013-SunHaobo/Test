package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class MathUtil {

	/**
	 * Get a random integer from [start, end] inclusive
	 * @param start
	 * @param end
	 * @param random
	 * @return
	 */
	public static int getRandomInteger(int start, int end, Random random){
	    if ( start > end ) {
	      throw new IllegalArgumentException("Start cannot exceed End.");
	    }
	    //get the range, casting to long to avoid overflow problems
	    long range = (long)end - (long)start + 1;
	    // compute a fraction of the range, 0 <= frac < range
	    long fraction = (long)(range * random.nextDouble());
	    int randomNumber =  (int)(fraction + start);
	    return randomNumber;
	  }
	
	/**
	 * Get unique random integers List, sorted ascending, start to end inclusive
	 * @param start
	 * @param end
	 * @param random
	 * @param totalInts
	 * @return
	 */
	public static List<Long> getUniqueRandomLongSet(long start, long end, Random random, int totalInts) {
		List<Long> wholeList = new ArrayList<Long>();
		
		for(long i=start; i<=end; i++){
			wholeList.add(i);
		}
		Collections.shuffle(wholeList, random);
		List<Long> resultList = wholeList.subList(0,totalInts);
		Collections.sort(resultList);
		return resultList;
	}	

    public static List<Integer> getUniqueRandomIntSet(int start, int end, Random random, int totalInts) {
        List<Integer> wholeList = new ArrayList<Integer>();

        for (int i = start; i <= end; i++) {
            wholeList.add(i);
        }
        Collections.shuffle(wholeList, random);
        List<Integer> resultList = wholeList.subList(0, totalInts);
        Collections.sort(resultList);
        return resultList;
    }
	
	
}	