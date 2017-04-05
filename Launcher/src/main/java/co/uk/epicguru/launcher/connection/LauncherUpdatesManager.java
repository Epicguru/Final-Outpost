package co.uk.epicguru.launcher.connection;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JProgressBar;

import org.apache.commons.io.FileUtils;

import co.uk.epicguru.launcher.Main;
import co.uk.epicguru.launcher.frame.DownloadProgress;
import co.uk.epicguru.launcher.frame.OkCancel;

public final class LauncherUpdatesManager {
	private LauncherUpdatesManager(){ }
	private static boolean close;
	private static boolean ok;
	
	
	public static void downloadLatest() throws Exception{	
		Main.checkConnection();		
		
		String[] lines = General.readLines(Main.base, Main.launcher, Main.latestLauncher);
		
		String version = null;
		String url = null;
		
		int index = 0;
		for(String line : lines){
			
			// Line 0 : Version
			// Line 1 : URL to new version
			
			if(index == 0){
				version = line;				
			}else if(index == 1){
				url = line;				
			}else{
				Main.print("Unknown string in launcher version txt : " + line);
			}
			
			index++;
		}
		
		//version = "asd123";
		//url = Main.base + Main.versions + "0.0.3/Game.jar";
		
		// Check for update
		if(version.equals(Main.VERSION)){
			// Up to date
			Main.print("The launcher is up to date!");
		}else{
			// Update available!
			Main.print("An update to the launcher is available!",
					version, "is ready to download!",
					"Current version is", Main.VERSION);
			
			// Check to see if DOWN
			if(url.equals(Main.linkDown)){
				Main.print("The link is down for maintenance! Aborting update!");
				return;
			}
			
			// Download update
			Main.print("Downloading new version from", url);
			
			downloadNewJar(version, url);
		}
	}
	
	public static void downloadNewJar(String name, String location) throws Exception{
		ok = false;
		Runnable ok = () -> {
			LauncherUpdatesManager.ok = true;
		};
		Runnable cancel = () -> {
			LauncherUpdatesManager.ok = false;
		};	
		
		new OkCancel(ok, cancel, "Launcher update available!", "A new launcher update is available. This update is not optional,\n"
				+ "but you can install it later if you wish. Do you want to install it now?");
		
		if(LauncherUpdatesManager.ok){
			
			// UI
			DownloadProgress pr = new DownloadProgress();
			pr.setVisible(true);
			JProgressBar bar = pr.progressBar;
			
			// Ok, download!
			
			URL url = General.newURL(location);
			InputStream is = url.openStream();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			int estimate = getFileSize(url);
			pr.nameOfDownload.setText(name + " (" + FileUtils.byteCountToDisplaySize(estimate) + ')');
			int total = 0;
			
			Main.print("File size", estimate);
			
			int n = 0;
			byte[] chunk = new byte[(int) (10 * FileUtils.ONE_KB)]; // 10 KB
			
			while((n = is.read(chunk)) > 0){
				
				if(close){
					// Window closed
					return;
				}
				
				out.write(chunk, 0, n);
				total += n;
				
				float p = total / (float)estimate;
				p *= 100;
				
				bar.setMaximum(100);
				bar.setValue((int)p);
			}
			
			Main.print("Estimated", estimate, "bytes, there were", total, "bytes.");
			
			byte[] bytesRead = out.toByteArray();
			out.close();
			
			// Save to file
			File file = new File(Main.getFolder().getAbsolutePath() + "\\" + name + ".jar");
			bytesToFile(bar, bytesRead, file);
			
			// Execute new and delete this.			
			is.close();
		}else{
			// Cancel
			Main.print("Download was not accepted, quitting...");
			System.exit(0);
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
		
		// Run
		runNewLauncher(file);
		
		Main.print("Downloaded and ran new launcher, exiting...");
		System.exit(0);
	}
	
	public static void runNewLauncher(File launcher) throws IOException{
		Main.cmd("java -jar \"" + launcher.getAbsolutePath() + '"' + " \"" + Main.getFile().getAbsolutePath() + '"');
	}
	
	public static void closingWindow() {
		close = true;
		Main.print("Window closed, ending program...");
		System.exit(0);
	}
}
