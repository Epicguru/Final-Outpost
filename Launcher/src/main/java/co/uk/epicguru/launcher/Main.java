package co.uk.epicguru.launcher;

import co.uk.epicguru.launcher.frame.Frame;

public class Main {

	public static final String base = "https://epicguru.github.io/Final-Outpost/";
	public static final String latestVersion = "Version.txt";
	
	public static void main(String... args){
		print("Hello world!");
		
		try{
			run();
			print("Run ended sucessfully!");
			
		}catch(RuntimeException e){
			print("Oh no! A", e.getClass().getName(), "was thrown!");
			print("TODO handle exception.");
			e.printStackTrace();
		}finally{
			cleanup();
		}
		
	}
	
	private static void cleanup() throws RuntimeException{
		
		
		print("Cleanup ended successfuly!");
	}
	
	private static void run() throws RuntimeException{
		Frame.run();
	}
	
	private static StringBuilder str = new StringBuilder();
	public static void print(Object... args){
		str.setLength(0);
		for(Object o : args){
			str.append(o == null ? "null" : o.toString());
			str.append(' ');
		}
		
		// Log here...
		
		System.out.println(str.toString());
		str.setLength(0);
	}
}
