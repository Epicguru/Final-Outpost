package co.uk.epicguru.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;

import co.uk.epicguru.IO.JLIOException;
import co.uk.epicguru.IO.JLineParsers;
import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.IO.JLineWriter;

public class Code {
	private static boolean shutdown;
	public static File currentPlugin;

	public static void run(Frame frame){

		JLineParsers.loadParsers();

		JLineReader reader = null;
		File props = new File("Plugin Helper/Properties.txt");
		props.getParentFile().mkdirs();
		try {
			props.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(-1);
		}
		try {
			reader = new JLineReader(props);
		} catch (JLIOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		reader.readAllLines();

		boolean autoSave = reader.read("Auto Save") == null ? false : (boolean)reader.read("Auto Save");
		String[] recent = reader.read("Recent") == null ? null : ((String[])reader.read("Recent")).length == 0 ? null : (String[])reader.read("Recent");

		frame.autoSave.setSelected(autoSave);
		
		frame.recentMenu.removeAll();
		for(String string : recent){
			
			if(string == null || string.isEmpty() || string.trim().isEmpty())
				continue;
			
			if(!new File(string).exists()){
				continue;
			}
			
			frame.recentMenu.add(new JMenuItem(string)).addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Selected
					recent(string, frame);
				}
			});
			
		}
		
		refresh(frame);

		reader.dispose();

		frame.refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh(frame);	
			}
		});
		
		frame.fromFileMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// From File pressed
				System.out.println("Pressed 'from file'");
				
				// Open browser
				JFileChooser browser = new JFileChooser(new File("\\").getParentFile());
				browser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int val = browser.showOpenDialog(frame);
				if(val == JFileChooser.APPROVE_OPTION){
					System.out.println(browser.getSelectedFile().getAbsolutePath());
					String result = load(browser.getSelectedFile());
					System.out.println("Result : " + (result == null ? "Good!" : result));
					if(result == null){
						currentPlugin = browser.getSelectedFile();
						frame.recentMenu.add(new JMenuItem(browser.getSelectedFile().getAbsolutePath())).addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								// Selected
								recent(browser.getSelectedFile().getAbsolutePath(), frame);
							}
						});
						refresh(frame);
					}
				}
			}
		});

		// Autosaving
		Thread thread = new Thread(() -> {
			while(!shutdown){
				if(frame.autoSave.isSelected()){
					try {
						Thread.sleep(1000 * 60);
						if(frame.autoSave.isSelected()){
							System.out.println("Autosaving...");
							save(frame);
						}
					} catch (Exception e1) {

					}
				}
			}
		});
		thread.start();
	}
	
	public static void refresh(Frame frame){
		if(frame.recentMenu.getItemCount() == 0){
			frame.recentMenu.setEnabled(false);
		}else{
			frame.recentMenu.setEnabled(true);
		}
		if(currentPlugin == null){
			frame.saveMenuButton.setEnabled(false);
			frame.refresh.setEnabled(false);
		}else{
			frame.saveMenuButton.setEnabled(true);
			frame.refresh.setEnabled(true);
		}
	}
	
	public static void recent(String file, Frame frame){
		File file2 = new File(file);
		System.out.println("Opening recent..." + file2.getAbsolutePath());
		
		String result = load(file2);
		System.out.println("Result : " + (result == null ? "Good!" : result));
		if(result == null){
			// Good to go!
			currentPlugin = file2;
			refresh(frame);
		}else{
			// Error
		}
		
	}
	
	public static String load(File file){
		if(!file.exists()){
			return "Directory does not exist!";
		}
		if(!file.isDirectory()){
			return "That is a file, not a directory! Please give the root folder.";
		}
		if(file.list().length == 0){
			return "Directory is empty";
		}
		
		return null;
	}

	public static void save(Frame frame){
		if(currentPlugin == null)
			return;

		// END
		try {
			File props = new File("Plugin Helper/Properties.txt");
			JLineWriter writer = new JLineWriter(props);
			String[] strings = new String[frame.recentMenu.getItemCount()];
			for(int i = 0; i < strings.length; i++){
				strings[i] = frame.recentMenu.getItem(i).getText();
			}
			writer.writeLine("Auto Save", frame.autoSave.isSelected());
			writer.writeLine("Recent", strings);
			writer.save();

			System.out.println("Exit");
		} catch (Exception e) {
			e.printStackTrace();
		}	

	}

	public static void close(Frame frame) {

		shutdown = true;

		save(frame);	
	}	
}
