package co.uk.epicguru.launcher;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import co.uk.epicguru.launcher.connection.General;
import co.uk.epicguru.launcher.frame.Frame;

public class Main {

	public static final String VERSION = "Launcher v0";	
	
	public static final String base = "https://epicguru.github.io/Final-Outpost/";
	public static final String linkDown = "DOWN";
	public static final String pending = "PENDING_UPDATE/";
	public static final String launcher = "Launcher/";
	public static final String latestLauncher = "Latest.txt";
	public static final String versions = "Versions/";
	public static final String versionsLatest = "Versions.txt";
	
	public static void main(String... args){
		print("Hello world!");
		print("Working from", Main.getFile());
		
		try{
			run();
			
			// TODO wait until close
			
			print("Run ended sucessfully!");
			
		}catch(RuntimeException e){
			print("Oh no! A", e.getClass().getName(), "was thrown!");
			print("TODO handle exception.");
			JOptionPane.showConfirmDialog(null,
					e.getClass().getName() + "' w/ message \n'" + e.getMessage() + "'",
					"Exception in launcher", 
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}finally{
			cleanup();
		}
		
	}
	
	private static void cleanup() throws RuntimeException{
		
		
		print("Cleanup ended successfuly!");
	}
	
	private static void run() throws RuntimeException{
		
		checkConnection();
		
		Frame.run();
	}
	
	public static void cmd(String command){
		
		Main.print("Running", command);
		
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static File getFile(){
		return new File(Main.class.getProtectionDomain()
				  .getCodeSource()
				  .getLocation()
				  .getPath());				
	}
	
	public static File getFolder(){
		return getFile().getParentFile();
	}
	
	public static void checkConnection(){
		if(!General.isConnected()){
			int option = -1;
			boolean gotConnection = false;
			while(option != JOptionPane.CANCEL_OPTION){
				option = JOptionPane.showConfirmDialog(null,
						"No connection to the server!\nPlease check your internet connection!\nDo you want to attempt to connect again?",
						"No Connection", 
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.ERROR_MESSAGE);
				if(option == JOptionPane.OK_OPTION){
					if(General.isConnected()){
						// Stop
						gotConnection = true;
						break;
					}
				}
			}
			if(!gotConnection){
				// TODO improve me
				cleanup();
				System.exit(0);
			}
		}
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
