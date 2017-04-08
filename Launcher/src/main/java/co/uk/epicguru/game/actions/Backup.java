package co.uk.epicguru.game.actions;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import org.apache.commons.io.FileUtils;

import co.uk.epicguru.launcher.Main;
import co.uk.epicguru.launcher.frame.Frame;

public class Backup {
	
	public static boolean backup(Frame frame){
		
		frame.getRunBackup().setEnabled(false);
		
		LocalDate date = LocalDate.now();
		String name = date.toString();
		
		
		
		File source = new File(Main.gameDataDir);
		if(!source.exists()){
			frame.getRunBackup().setEnabled(true);		
			Main.print("Source (" + source.getAbsolutePath() + ") does not exist!");
			return false;
		}
		
		Main.print("Backup saving as", name);
		
		File destination = new File(Main.gameBackup + name);		
		if(destination.exists()){
			try {
				FileUtils.deleteDirectory(destination);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		destination.mkdirs();		
		
		try {
			FileUtils.copyDirectory(source, destination);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		frame.getRunBackup().setEnabled(true);
		Main.print("Done!");
		return true;
	}	
}
