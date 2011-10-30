package modules.at.model;


/**
 * Only keep the latest added maxsize elements
 * Using Array to implement a fixed length Queue,
 * newly inserted elements are added at the end, 
 * when queue is full, head element is removed
 */
public class FixedLengthQueue {
	private Object[] array;
	private int start, end, cur;
	
	public FixedLengthQueue(int maxsize) {
		array = new Object[maxsize];
		start = 0;
		end = 0;
	}


	public void insert(Object o) {
		
		
	}

	
	
}