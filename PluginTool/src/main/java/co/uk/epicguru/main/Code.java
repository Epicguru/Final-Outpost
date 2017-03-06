package co.uk.epicguru.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JMenuItem;

import co.uk.epicguru.IO.JLIOException;
import co.uk.epicguru.IO.JLineParsers;
import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.IO.JLineWriter;

public class Code {
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
		String[] recent = reader.read("Recent") == null ? new String[0] : (String[])reader.read("Recent");

		frame.autoSave.setSelected(autoSave);
		for(String string : recent){
			frame.recentMenu.add(new JMenuItem(string)).addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Selected
					System.out.println("Selected to load a " + string);
				}
			});;

		}

		reader.dispose();

		Thread thread = new Thread(() -> {
			frame.fromFileMenu.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// From File pressed
					System.out.println("Pressed 'from file'");
				}
			});
		});
		thread.start();
	}

	public static void close(Frame frame) {
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
}
