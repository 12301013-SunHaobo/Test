package others;

import java.io.PrintStream;

public class TestEncoding {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		/*
		System.out.println("\u201cHe\u2019s got a very loyal constituency, but the national stage has diminished him as a candidate,\u201d Mr. Hagan said."+
"Mr. Cameron\u2019s private home and political constituency are only a short drive away, as is the prime minister\u2019s official country residence, Chequers."+
"For the first time indeed in English history popular constituencies had been organized by that Measure."+
"\u201cHe\u2019s not being subtle about wanting to claim credit and to win favor with constituencies,\u201d said Julian Zelizer, a presidential historian at Princeton University."+
"Unions are a traditional Democratic constituency, providing support with campaign spending and volunteers.");
		*/

		System.out.println("u2014"+" -> "+"\u2014");
		System.out.println("u2018"+" -> "+"\u2018");
		System.out.println("u2019"+" -> "+"\u2019");
		System.out.println("u201c"+" -> "+"\u201c");
		System.out.println("u201d"+" -> "+"\u201d");
		System.out.println("u2026"+" -> "+"\u2026");
		System.out.println("u2032"+" -> "+"\u2032");
		
	}

}
