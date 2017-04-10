package co.uk.epicguru.game;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JProgressBar;

import org.apache.commons.io.FileUtils;

import co.uk.epicguru.launcher.Main;
import co.uk.epicguru.launcher.connection.General;
import co.uk.epicguru.launcher.frame.Frame;

public class GameLoader {

	public static void createDirectory(){
		File file = new File(Main.gameDir);
		if(!file.exists() || !file.isDirectory()){
			file.mkdirs();
		}
		
		file = new File(Main.pluginsDedicated);
		if(!file.exists() || !file.isDirectory()){
			file.mkdirs();
		}		
	}

	private static void accomodate(String version){
		File directory = new File(Main.gameDir + Main.gameVersions + version);

		if(!directory.exists() || !directory.isDirectory()){
			directory.mkdirs();
		}
	}

	public static String getCurrentFromFile() throws Exception{
		File file = new File(Main.gameDir + Main.currentGameVersion);
		
		if(!file.exists()){
			return null;
		}
		
		String line = null;
		@SuppressWarnings("unchecked")
		List<String> lines = FileUtils.readLines(file);
		
		if(lines.size() != 1){
			return null;
		}
		
		line = lines.get(0);
		
		Main.print("Got", line, "from", file.getAbsolutePath());
		
		return line;
	}
	
	public static void saveCurrentToFile(String value) throws Exception{
		
		Main.print("Saving current version to file :", value);
		
		File file = new File(Main.gameDir + Main.currentGameVersion);
		
		if(!file.exists()){
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		
		List<String> list = new ArrayList<String>();
		list.add(value);
		FileUtils.writeLines(file, list);
		
		Main.print("Done!");
	}
	
	private static boolean swap(String newVersion, boolean installCustomPlugins) throws IOException{

		// Get version
		File version = new File(Main.gameDir + Main.gameVersions + newVersion + '/' + Main.FOE);

		Main.print("Activating", version.getAbsolutePath());

		if(!version.exists()){
			Main.print("Version was not found, canceling activation and stopping...");
			return false;
		}
				
		// Plugins
		File plugins = new File(Main.gameDir + Main.gameVersions + newVersion + '/' + "Plugins/");
		File dest = new File(Main.gameDataDir + "Plugins/");
		FileUtils.deleteDirectory(dest);
		dest.mkdir();
		if(plugins.exists()){
			FileUtils.copyDirectory(plugins, dest);
		}else{
			Main.print("Plugins folder does not exist!");
		}
		
		if(installCustomPlugins){
			Main.print("Installing custom plugins...");			
			File custom = new File(Main.pluginsDedicated);
			
			if(custom.exists()){
				FileUtils.copyDirectory(custom, dest);
				Main.print("Done!");
			}else{
				Main.print("Custom plugins folder does not exist!");
			}
			
		}else{
			Main.print("Custom plugins are disabled.");
		}
		
		Main.print("Done!");

		return true;
	}
	
	private static boolean downloadFromServer(Frame frame, String version) throws Exception{
		Main.checkConnection();

		URL root = General.newURL(Main.base + Main.versions + version);
		if(!General.checkConnectionTo(root.toString())){
			// Not found, but we are connected
			return false;
		}

		URL gameJar = General.newURL(Main.base + Main.versions + version + "/Game.jar");

		// Download - Quick for now
		accomodate(version);
		File destination = new File(Main.gameDir + Main.gameVersions + version + '/' + Main.FOE);
		Main.print("Downloading", gameJar.toString());
		copyURL(frame, gameJar, destination);
		
		// Download plugins
		Main.print("Now downloading plugins...");
		downloadPlugins(version);		

		return true;
	}
	
	public static void downloadPlugins(String version) throws Exception {
		String[] names = getPluginNamesFor(version);
		URL[] urls = getPluginsFor(version);
		
		int index = 0;
		for(URL url : urls){
			
			File destination = new File(Main.gameDir + Main.gameVersions + version + "/Plugins/" + names[index]);
			Main.print("Saving to", destination.getAbsolutePath());
			
			// Delete old (if exists)
			if(destination.exists()){
				if(destination.isDirectory()){
					FileUtils.deleteDirectory(destination);
				}else{
					FileUtils.forceDelete(destination);
				}
			}
			
			// Copy
			FileUtils.copyURLToFile(url, destination);
			
			index++;
		}
	}

	public static boolean downloadVersion(Frame frame, String version) throws Exception {
		boolean copy = frame.getCheckBox().isSelected();
		if(swap(version, copy)){
			// Done
			return true;
		}else{
			// Download and swap
			if(!downloadFromServer(frame, version)){
				// Problem...
				return false;
			}

			// Swap
			swap(version, copy);
			return true;
		}
	}

	private static void deleteExtracted() throws IOException{
		File file = new File(Main.gameDataDir + "/Extracted");
		
		FileUtils.deleteDirectory(file);
	}
	
	public static boolean runCurrent(Frame frame) throws Exception{

		String current = getCurrentFromFile();
		if(current == null){
			current = frame.getVersionSelection().getSelectedItem().toString();
			saveCurrentToFile(current);
		}else{
			String selected = frame.getVersionSelection().getSelectedItem().toString();			
			String read = current;
			
			if(!selected.equals(read)){
				// We are selecting one that is not marked as current, meaning that we just changed version.
				// Now we need to update the 'current' version to the selected one, and also delete /Extracted data.
				
				// Save new
				saveCurrentToFile(selected);
				
				// Delete old /Extracted data, because it will not automatically reload.
				deleteExtracted();
			}
		}
		
		File currentFile = new File(Main.gameDir + Main.versions + current + '/' + Main.FOE);

		if(!currentFile.exists() || currentFile.isDirectory()){
			Main.print("Current version is not named correctly, does not exist or is a directory!");
			return false;
		}else{
			Main.print("Running current version!");
			Main.cmd("java -jar \"" + currentFile.getAbsolutePath() + "\"");
			return true;
		}		
	}
	
	private static int getFileSize(URL url) {
	    HttpURLConnection conn = null;
	    try {
	        conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("HEAD");
	        //conn.getInputStream();
	        return conn.getContentLength();
	    } catch (IOException e) {
	        return -1;
	    } finally {
	        conn.disconnect();
	    }
	}

	private static void copyURL(Frame frame, URL source, File destination) throws Exception{
		// Ok, download!
		frame.setBar(0f);
		InputStream is = source.openStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		int estimate = getFileSize(source);
		int total = 0;

		Main.print("File size", estimate, "from", source.toString());

		int n = 0;
		byte[] chunk = new byte[(int) (10 * FileUtils.ONE_KB)]; // 10 KB
		
		while((n = is.read(chunk)) > 0){

			out.write(chunk, 0, n);
			total += n;

			float p = total / (float)estimate;

			frame.setBar(p);
		}

		Main.print("Estimated", estimate, "bytes, there were", total, "bytes.");

		byte[] bytesRead = out.toByteArray();
		out.close();

		// Save to file
		bytesToFile(frame.getProgressBar(), bytesRead, destination);

		// Execute new and delete this.			
		is.close();
	}
	
	public static void bytesToFile(JProgressBar bar, byte[] array, File file) throws Exception{
		
		if(file.isDirectory()){
			return;
		}
		
		if(file.exists()){
			file.delete();
		}
		
		Main.print("Writing to", file.getAbsolutePath());
		
		if(!file.exists()){
			if(file.getParentFile() != null){
				file.getParentFile().mkdirs();
			}else{
				// ERROR?
				Main.print("Error : Parent file of " + file.getAbsolutePath() + " was null!");
				return;
			}
		}	
		boolean worked = file.createNewFile();
		Main.print("Created new file :", worked);
		
		// UI responsiveness
		bar.setIndeterminate(true);
		
		// Save to disk, it is all in memory
		FileUtils.writeByteArrayToFile(file, array);
		
		Main.print("Downloaded and ran file.");
	}

	public static void play(Frame frame) {
		try {
			boolean worked = downloadVersion(frame, frame.getVersionSelection().getSelectedItem().toString());

			if(!worked){
				Main.print("Download or activation of version did not work!");
			}else{
				Main.print("Worked! Yay!");
				runCurrent(frame);
				System.exit(0);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
	}

	public static String[] getPluginNamesFor(String version) throws Exception {
		return General.readLines(Main.base + Main.versions + version + "/" + Main.pluginsList);
	}
	
	public static URL[] getPluginsFor(String version) throws Exception{		
		
		String[] lines = getPluginNamesFor(version);
		URL[] urls = new URL[lines.length];
		
		int index = 0;
		for(String line : lines){
			Main.print("Pending :", line);
			
			// Get URL
			urls[index] = General.newURL(Main.base + Main.versions + version + "/" + "Plugins/" + line);
			
			// Next
			index++;
		}
		
		return urls;
	}
}
