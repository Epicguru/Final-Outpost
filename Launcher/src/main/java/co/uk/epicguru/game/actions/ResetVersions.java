package co.uk.epicguru.game.actions;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import co.uk.epicguru.launcher.Main;
import co.uk.epicguru.launcher.frame.Frame;

public class ResetVersions {

	public static boolean resetVersions(Frame frame){
		Main.print("Deleting all versions (NOT GAME DATA!)");
		try {
			FileUtils.deleteDirectory(new File(Main.gameDir));
			Main.print("Done!");
			return true;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
