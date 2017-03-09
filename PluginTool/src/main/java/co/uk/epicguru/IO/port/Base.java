package co.uk.epicguru.IO.port;

public class Base {
	
	public void print(String text){
		System.out.println(text);
	}
	
	public void error(String text){
		System.out.print("ERROR : " + text);
	}
	
	public void error(String text, Exception e){
		e.printStackTrace();
		System.out.print("ERROR E : " + text);
	}
	
}
