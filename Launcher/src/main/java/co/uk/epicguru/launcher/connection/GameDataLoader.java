package co.uk.epicguru.launcher.connection;

import co.uk.epicguru.launcher.Main;
import co.uk.epicguru.launcher.frame.Frame;

public final class GameDataLoader {

	private static Frame frame;
	
	public static void run(Frame frame) throws Exception {
		GameDataLoader.frame = frame;
		
		refresh();
	}
	
	public static void refresh() throws Exception{
		Main.print("Refresh...");
		setNews();	
	}
	
	public static void setNews() throws Exception{
		
		Main.print("Setting news");
		
		StringBuilder stringBuilder = new StringBuilder();
		
		String[] lines = General.readLines(Main.base, Main.extra, Main.news);
		for(String line : lines){
			stringBuilder.append(line);
			stringBuilder.append('\n');
		}
		frame.getNews().setText(stringBuilder.toString());
	}
	
}
