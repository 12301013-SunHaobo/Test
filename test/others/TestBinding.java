package others;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

class ParentCF { //parent Classifier
	public void classify(Set s) {
		System.out.println("Set");
	}
}
class ChildCF extends ParentCF {  //child Classifier
	public void classify(Collection c) {
		System.out.println("Unknown Collection");
	}
}

class Top {
    //public void f(Object o) {System.out.println("TopObj");}
    public void f(Integer i) {System.out.println("integer");}
}
class Sub extends Top {
    public void f(Object o) {System.out.println("SubObj [overriden]");}
    //public void f(String s) {System.out.println("SubString [overloaded]");}
}

public class TestBinding {
    public static void main(String[] args) {  
    	//testTopSub();
    	testParentChild();
    	
    }
    private static void testTopSub(){
        //Top top = new Sub();
        Sub sub = new Sub();
        Top top = sub; 
        	
        String str = "Something";
        Object obj = str;

        //top.f(obj);//topObj
        //top.f(str);//topObj
        sub.f(obj);//subObj
        sub.f(8);//subString
    }    
	private static void testParentChild() {

		Set[] tests = new Set[] { 
				new HashSet(), // A Set
				//new ArrayList(), // A List
				//new HashMap().values() // Neither Set nor List
		};
		ChildCF cf = new ChildCF();
		for (int i = 0; i < tests.length; i++) {
			cf.classify(tests[i]);
		}
	}
    
    

}


