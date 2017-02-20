package co.uk.epicguru.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;

import co.uk.epicguru.main.FOE;

public final class Log {

	private static ArrayList<String> log = new ArrayList<String>();
	
	private static boolean defaultPrint(String tag, String text){
		if(Gdx.app == null){
			System.out.println(tag + " : " + text);
			return true;
		}
		return false;
	}
	
	/**
	 * Logs an info message.
	 * @param tag The tag to submit the message with.
	 * @param text The text to log.
	 */
	public static void info(String tag, String text) {
		
		if(defaultPrint(tag, text))
			return;
		
		Gdx.app.log(tag, text);
		log.add("[" + tag + "][INFO] " + text);
	}

	/**
	 * Logs an debug message.
	 * @param tag The tag to submit the message with.
	 * @param text The text to log.
	 */
	public static void debug(String tag, String text) {
		
		if(defaultPrint(tag, text))
			return;
		
		Gdx.app.debug(tag, text);
		log.add("[" + tag + "][DEBUG] " + text);
	}

	/**
	 * Logs an error message.
	 * @param tag The tag to submit the message with.
	 * @param text The text to log.
	 */
	public static void error(String tag, String text) {
		
		if(defaultPrint(tag, text))
			return;
		
		Gdx.app.error(tag, text);
		log.add("[" + tag + "][ERROR] " + text);
	}
	
	/**
	 * Logs an error message.
	 * @param tag The tag to submit the message with.
	 * @param text The text to log.
	 * @param e The exception to log details upon.
	 */
	public static void error(String tag, String text, Exception e) {
		
		if(defaultPrint(tag, text))
			return;
		
		Gdx.app.error(tag, text, e);
		log.add("[" + tag + "][ERROR] " + text);
		log.add("Exception info:");
		log.add("Class - " + e.getClass().getSimpleName());
		log.add("Message - " + e.getMessage());
		for(StackTraceElement element : e.getStackTrace()){
			log.add("  " + element.toString());			
		}
	}

	/**
	 * Sets the log level
	 * @param level The Gdx.app. level to debug at.
	 */
	public static void setLogLevel(int level){
		Gdx.app.setLogLevel(level);
		log.add("Setting log level to " + level);
	}
	
	/**
	 * Saves the log to disk for later reference.
	 */
	@SuppressWarnings("resource")
	public static void saveLog(){
		
		Log.info("Logging", "Saving log...");
		
		// Time and date
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
		LocalDateTime now = LocalDateTime.now();
		
		String name = dtf.format(now);
		Log.debug("Logging", "Saving log as " + FOE.gameDirectory + FOE.logsDirectory + name + FOE.logsExtension);

		File saveFile = new File(FOE.gameDirectory + FOE.logsDirectory + name + FOE.logsExtension);
		
		// Make dirs...
		saveFile.getParentFile().mkdirs();
		
		try {
			saveFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(saveFile));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		for(int i = 0; i < log.size(); i++){
			try {
				writer.write(log.get(i));
				writer.newLine();
			} catch (IOException e) {
				return;
			}
		}
		
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {

		}
	}
	
}
