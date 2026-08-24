package org.elis.homeservices.utility;

public class MainTest {

	public static void main(String[] args) {
		String codice = "";
		for(int i=0;i<6;i++) {
			int n= 10;
			int casuale= (int) (Math.floor(Math.random()*n));
			codice+=casuale;
		}
		
		System.out.print(codice);
    }
}
