package others.nio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TestSocket {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
            Socket s = new Socket("www.google.com", 80);

            BufferedReader in = new BufferedReader(new 
                InputStreamReader(s.getInputStream()));
            PrintWriter socketOut = new PrintWriter(s.getOutputStream());

            //socketOut.print("GET /index.html\n\n");
            socketOut.print("GET \n\n");
            socketOut.flush();

            String line;

            while ((line = in.readLine()) != null){
                System.out.println(line);
            }

        } catch (Exception e){}
	}

}
