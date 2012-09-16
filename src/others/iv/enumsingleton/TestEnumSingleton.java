package others.iv.enumsingleton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class TestEnumSingleton {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		ESingleton es = ESingleton.INSTANCE;
		
		es.setAge(1);
		System.out.println("age="+es.getAge());
		
		//es.setAge(2);
		//System.out.println("age="+es.getAge());
		
		
		String filename = "D:/projects/java/workspace/Test/src/others/iv/enumsingleton/ESingleton-tmp.txt";
		
		//store(es, new File(filename));
		
		ESingleton es2 = (ESingleton)load(new File(filename));
		
		System.out.println("age="+es2.getAge());
		
		
		
	}

	private static void store(Serializable o, File f) throws IOException {
		ObjectOutputStream out = // The class for serialization
		new ObjectOutputStream(new FileOutputStream(f));
		out.writeObject(o); // This method serializes an object graph
		out.close();
	}

	private static Object load(File f) throws IOException, ClassNotFoundException {
		ObjectInputStream in = // The class for de-serialization
		new ObjectInputStream(new FileInputStream(f));
		return in.readObject(); // This method deserializes an object graph
	}
}
