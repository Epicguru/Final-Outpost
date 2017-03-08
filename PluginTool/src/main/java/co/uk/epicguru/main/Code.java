package co.uk.epicguru.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.apache.commons.io.FileUtils;

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
			frame.paths.setModel(new DefaultTreeModel(addNodes(new DefaultMutableTreeNode(), currentPlugin)));
			for(TreeSelectionListener listener : frame.paths.getListeners(TreeSelectionListener.class)){
				frame.paths.removeTreeSelectionListener(listener);
			}
			frame.paths.addTreeSelectionListener(new TreeSelectionListener() {
				public void valueChanged(TreeSelectionEvent e) {
					
					if(currentPlugin == null){
						return;
					}
					
					TreeNode[] path = ((DefaultMutableTreeNode)e.getPath().getLastPathComponent()).getPath();
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append(currentPlugin.getAbsolutePath() + '\\');
					int index = -1;
					for(TreeNode node : path){
						index++;
						if(index < 2){
							continue;
						}
						stringBuilder.append(node.toString());
						if(!node.toString().equals(e.getPath().getLastPathComponent().toString()))
							stringBuilder.append('\\');
					}
					System.out.println(stringBuilder.toString());
					File file = new File(stringBuilder.toString());
					
					frame.codeInfo.setText("");
					
					if(!file.exists()){
						frame.codeInfo.setText("File or folder could not be found!");
						return;
					}
					
					if(file.isDirectory()){
						StringBuilder str = new StringBuilder();
						str.append("Directory '");
						str.append(file.getName());
						str.append('\'');
						str.append('\n');
						
						frame.codeInfo.setText(str.toString());					
						
					}else{
						frame.codeInfo.setText("File '" + file.getName() + "'\n" + FileUtils.sizeOf(file) / (float)FileUtils.ONE_MB + " megabytes.");
					}
				}
			});
		}
	}
	
	public static int getFiles(Frame frame, File file, String name){
		if(frame.folderStats.isSelected())
			return FileUtils.listFiles(file, new String[]{name}, true).size();
		else
			return -1;
	}
	
	 @SuppressWarnings({ "unchecked", "rawtypes" })
 	public static DefaultMutableTreeNode addNodes(DefaultMutableTreeNode curTop, File dir) {
		    String curPath = dir.getPath();
		    DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(dir.getName());
		    if (curTop != null) { // should only be null at root
		      curTop.add(curDir);
		    }
		    Vector ol = new Vector();
		    String[] tmp = dir.list();
		    for (int i = 0; i < tmp.length; i++)
		      ol.addElement(tmp[i]);
		    Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
		    File f;
		    Vector files = new Vector();
		    // Make two passes, one for Dirs and one for Files. This is #1.
		    for (int i = 0; i < ol.size(); i++) {
		      String thisObject = (String) ol.elementAt(i);
		      String newPath;
		      if (curPath.equals("."))
		        newPath = thisObject;
		      else
		        newPath = curPath + File.separator + thisObject;
		      if ((f = new File(newPath)).isDirectory())
		        addNodes(curDir, f);
		      else
		        files.addElement(thisObject);
		    }
		    // Pass two: for files.
		    for (int fnum = 0; fnum < files.size(); fnum++)
		      curDir.add(new DefaultMutableTreeNode(files.elementAt(fnum)));
		    return curDir;
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
