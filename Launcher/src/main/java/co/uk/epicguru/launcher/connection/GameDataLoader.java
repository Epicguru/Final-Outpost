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
		
		Main.checkConnection();
		
		setNews();	
		setVersions();
		setSplash();
	}
	
	@SuppressWarnings("unchecked")
	public static void setVersions() throws Exception{
		Main.print("Setting versions");
		
		String[] lines = General.readLines(Main.base, Main.versions, Main.versionsLatest);
		frame.getVersionSelection().removeAllItems();
		for(String line : lines){
			frame.getVersionSelection().addItem(line);
		}
		
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
	
	public static void setSplash() throws Exception{
		Main.print("Setting splash");
		
		String[] lines = General.readLines(Main.base, Main.extra, Main.splash);
		int index = (int) (Math.random() * lines.length - 1);
		if(index < lines.length){
			frame.getSplash().setText(lines[index]);
		}else{
			frame.getSplash().setText("No splash today :(");
		}
	}	
}
