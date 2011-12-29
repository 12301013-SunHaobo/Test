package modules.at.model;


/**
 * Only keep the latest added maxsize elements
 * Using Array to implement a fixed length Queue,
 * newly inserted elements are added at the end, 
 * when queue is full, head element is removed
 * 
 * Java has linked list queue, but they provide sequential access.
 * This implementation provide random access, and lightweight.
 */
public class FixedLengthQueue<T> {
	private Object[] array;
	private int head=0;
	private int tail=-1;
	private int size=0;
	
	public FixedLengthQueue(int fixedSize) {
		array = new Object[fixedSize];
	}

	//add to tail
	public void add(Object o) {
	    if(size<array.length){
	        size++;
	    }
	    tail++;
	    tail = tail%array.length;
        array[tail] = o;
        if(tail==head && size==array.length){
            head = ++head%array.length;
        }
	}

	//remove from head
	public void remove(){
	    if(size>0){
    	    head = ++head%array.length;
    	    size--;
	    }
	}
	
	public Object get(int i){
	    return array[(head+i)%array.length];
	}
	
	public int size(){
	    return this.size;
	}
}