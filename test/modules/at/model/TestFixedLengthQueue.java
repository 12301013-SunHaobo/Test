package modules.at.model;

import junit.framework.TestCase;


public class TestFixedLengthQueue extends TestCase{

    public void testAddAndRemove() {
        FixedLengthQueue arr = new FixedLengthQueue(3);
        assertEquals(arr.size(), 0); //initial size is 0, without adding any elements
        arr.add("a");
        assertEquals(arr.size(), 1);
        arr.add("b");
        assertEquals(arr.size(), 2);
        arr.add("c");
        assertEquals(arr.size(), 3);
        arr.add("d");
        assertEquals(arr.size(), 3);
        arr.add("e");
        assertEquals(arr.size(), 3);
        arr.add("f");
        assertEquals(arr.size(), 3);
        arr.add("g");
        assertEquals(arr.size(), 3);
        //only last 3 elements are in the queue
        assertEquals("e", arr.get(0));
        assertEquals("f", arr.get(1));
        assertEquals("g", arr.get(2));
        //remove all of them
        arr.remove();
        assertEquals(arr.size(), 2);
        assertEquals("f", arr.get(0));
        assertEquals("g", arr.get(1));
        arr.remove();
        assertEquals(arr.size(), 1);
        assertEquals("g", arr.get(0));
        arr.remove();
        assertEquals(arr.size(), 0);
        //remove from empty queue, size is still 0
        arr.remove();
        assertEquals(arr.size(), 0);
    }

}
