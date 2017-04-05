package co.uk.epicguru.launcher;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import co.uk.epicguru.launcher.connection.General;
import co.uk.epicguru.launcher.frame.Frame;
import co.uk.epicguru.launcher.frame.OkCancel;

public class Main {

	public static final String VERSION = "Launcher v0";	

	public static final String base = "https://epicguru.github.io/Final-Outpost/";
	public static final String linkDown = "DOWN";
	public static final String pending = "PENDING_UPDATE/";
	public static final String launcher = "Launcher/";
	public static final String extra = "Extra/";
	public static final String news = "News.txt";
	public static final String splash = "Splash.txt";
	public static final String latestLauncher = "Latest.txt";
	public static final String versions = "Versions/";
	public static final String versionsLatest = "Versions.txt";
	
	protected static boolean breakConn;
	protected static boolean gotConnection;

	public static void main(String... args){
		print("Hello world!");
		print("Working from", Main.getFile());

		try{
			if(args.length > 0)
				removeOld(args[0]);
			run();			
			print("Run ended sucessfully!");

		}catch(RuntimeException e){
			print("Oh no! A", e.getClass().getName(), "was thrown!");
			print("TODO handle exception.");
			JOptionPane.showConfirmDialog(null,
					e.getClass().getName() + " with message \n'" + e.getMessage() + "'",
					"Exception in launcher", 
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}finally{
			cleanup();
			System.exit(0);
		}
	}

	private static void cleanup() throws RuntimeException{
		print("Cleanup ended successfuly!");
	}

	private static void removeOld(String path){
		File file = new File(path);
		if(!file.exists()){
			Main.print("File to-delete", file.getAbsolutePath() + "does not exist!");
			return;
		}
		if(file.isDirectory()){
			Main.print("File to-delete is directory!");
			return;
		}

		try {
			FileUtils.forceDelete(file);
		} catch (IOException e) {
			Main.print("Error removing old -- More that one instance??");
			throw new RuntimeException(e);
		}
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
		try {
			return new File(Main.class.getProtectionDomain()
					.getCodeSource()
					.getLocation().toURI());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}				
	}

	public static File getFolder(){
		return getFile().getParentFile();
	}

	public static void checkConnection(){
		if(!General.isConnected()){
			int option = -1;
			Main.gotConnection = false;
			while(option != JOptionPane.CANCEL_OPTION){
				
				// OK
				Runnable ok = () -> {
					if(General.isConnected()){
						Main.breakConn = true;
						Main.gotConnection = true;
						Main.print("Got connection!");
					}
				};
				
				// CANCEL
				Runnable cancel = () -> {
					Main.print("Cancel search for connection");
					System.exit(0);
				};
				
				// Stops here until option selected. Red X on window gives CANCEL.
				new OkCancel(ok, cancel, "No internet connection!", "The server was not found! Please ensure that you are connected.\n"
						+ "Attempt to connect again?");
				
				if(Main.breakConn)
					break;
			}
			Main.breakConn = false;
			if(!Main.gotConnection){
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
