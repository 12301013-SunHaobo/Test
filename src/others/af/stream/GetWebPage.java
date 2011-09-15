package others.af.stream;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class GetWebPage {

	  public static void main (String[] args) {
	  
	    try {
	      String thisLine;
	      URL u = new URL("http://www.wholesale-dress.net/sweet-round-collar-long-sleeve-lace-dress-black-g1169694.html");
	      DataInputStream theHTML = new DataInputStream(u.openStream());
	      while ((thisLine = theHTML.readLine()) != null) {
	        System.out.println(thisLine);
	      } // while loop ends here
	    }
	    catch (MalformedURLException e) {
	      System.err.println(e);
	    }
	    catch (IOException e) {
	      System.err.println(e);
	    }
	    
	  }

	}