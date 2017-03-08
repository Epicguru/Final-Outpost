package co.uk.epicguru.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

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
		boolean fileStats = reader.read("File Stats") == null ? true : (boolean)reader.read("File Stats");
		String[] recent = reader.read("Recent") == null ? new String[0] : (String[])reader.read("Recent");

		frame.autoSave.setSelected(autoSave);
		frame.folderStats.setSelected(fileStats);
		
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
		
		frame.saveMenuButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currentPlugin != null){
					save(frame);
				}
			}
		});		
		
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
						save(frame);
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
		frame.codeInfo.setText("");
		frame.codeText.setText("");
		if(frame.recentMenu.getItemCount() == 0){
			frame.recentMenu.setEnabled(false);
		}else{
			frame.recentMenu.setEnabled(true);
		}
		if(currentPlugin == null){
			frame.saveMenuButton.setEnabled(false);
			frame.refresh.setEnabled(false);
			frame.paths.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Root - Unable to load...")));
		}else{
			boolean worked = getPluginInfo(frame);
			if(!worked){
				currentPlugin = null;
				refresh(frame);
				return;
			}
			frame.saveMenuButton.setEnabled(true);
			frame.refresh.setEnabled(true);
			for(TreeSelectionListener listener : frame.paths.getListeners(TreeSelectionListener.class)){
				frame.paths.removeTreeSelectionListener(listener);
			}
			frame.paths.setModel(new DefaultTreeModel(addNodes(new DefaultMutableTreeNode(currentPlugin.getName()), currentPlugin)));
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
						frame.codeText.setText("");
						StringBuilder str = new StringBuilder();
						str.append("Directory '");
						str.append(file.getName());
						str.append('\'');
						str.append('\n');
						
						str.append("Size : ");
						str.append(String.format("%.2f", (FileUtils.sizeOfDirectory(file) / (float)FileUtils.ONE_MB)));
						str.append(" MB.");
						str.append('\n');
						
						int javaFiles = getFiles(frame, file, "java");
						int classFiles = getFiles(frame, file, "class");
						int textureFiles = getFiles(frame, file, "png");
						boolean doneOne = false;
						
						if(javaFiles > 0){
							str.append(javaFiles);
							str.append(" java files.");
							str.append('\n');
							doneOne = true;
						}
						if(classFiles > 0){
							str.append(classFiles);
							str.append(" binary files.");
							str.append('\n');
							doneOne = true;
						}
						if(textureFiles > 0){
							str.append(textureFiles);
							str.append(" texture files.");
							str.append('\n');
							doneOne = true;
						}
						
						int total = getAllFiles(frame, file);
						int types = javaFiles + classFiles + textureFiles;
						int remainder = total - types;
						if(doneOne && remainder > 0 && total > 0){
							str.append(remainder);
							str.append(" other files.");
							str.append('\n');
							str.append("TOTAL FILES : ");
							str.append(total);
							str.append('\n');
						}else{
							str.append("TOTAL FILES : ");
							str.append(total);
							str.append('\n');
						}
						
						frame.codeInfo.setText(str.toString());					
						
					}else{
						StringBuilder str = new StringBuilder();
						str.append("File '");
						str.append(file.getName());
						str.append('\'');
						str.append('\n');
						str.append("Size : ");
						str.append(String.format("%.2f", (FileUtils.sizeOf(file) / (float)FileUtils.ONE_MB)));
						str.append(" MB.");
						str.append('\n');
						frame.codeInfo.setText("Hey!");
						if(FilenameUtils.isExtension(file.getPath(), "png")){
							str.append("TEXTURE FILE");
							str.append('\n');
						}
						else if(FilenameUtils.isExtension(file.getPath(), "java") || FilenameUtils.isExtension(file.getPath(), "txt") || FilenameUtils.isExtension(file.getPath(), "gradle")){
							str.append("SOURCE FILE");
							str.append('\n');
							try {
								frame.codeText.setText(FileUtils.readFileToString(file, Charset.defaultCharset()));
							} catch (IOException e1) {
								e1.printStackTrace();
								StringBuilder str2 = new StringBuilder();
								str2.append(e.getClass().getSimpleName());
								str2.append(" - ");
								str2.append(e1.getMessage());
								str2.append('\n');
								for(StackTraceElement stackTraceElement : e1.getStackTrace()){
									str2.append('\t');
									str2.append(stackTraceElement.toString());
								}
								frame.codeText.setText(str2.toString());
							}
						}
						else if(FilenameUtils.isExtension(file.getPath(), "CLASS")){
							str.append("BINARY FILE");
							str.append('\n');
						}
						frame.codeInfo.setText(str.toString());
					}
				}
			});
		}
	}
	
	public static int getAllFiles(Frame frame, File file){
		if(frame.folderStats.isSelected())
			return FileUtils.listFiles(file, null, true).size();
		else
			return -1;
	}
	
	public static int getFiles(Frame frame, File file, String name){
		if(frame.folderStats.isSelected())
			return FileUtils.listFiles(file, new String[]{name}, true).size();
		else
			return -1;
	}
	
	public static boolean getPluginInfo(Frame frame){
		String whereItShouldBe = currentPlugin.getAbsolutePath() + File.separator + "build.gradle";
		File file = new File(whereItShouldBe);
		
		frame.pluginName.setText("Editing - Unable to load :(");
		
		if(!file.exists())
			return false;
		if(file.isDirectory())
			return false;
		if(!file.canRead())
			return false;
		
		try {
			List<String> lines = FileUtils.readLines(file, Charset.defaultCharset());
			String name = null;
			String version = null;
			String provider = null;
			String pluginClass = null;
			for(String line : lines){
				if(line.contains("project.ext.pluginName")){
					System.out.print("Found plugin name : ");
					name = line.substring(line.indexOf('"') + 1, line.lastIndexOf('"'));
					System.out.println(name);
				}else if(line.contains("project.ext.pluginVersion")){
					System.out.print("Found plugin version : ");
					version = line.substring(line.indexOf('"') + 1, line.lastIndexOf('"'));
					System.out.println(version);
				}
				else if(line.contains("project.ext.pluginProvider")){
					System.out.print("Found plugin provider : ");
					provider = line.substring(line.indexOf('"') + 1, line.lastIndexOf('"'));
					System.out.println(provider);
				}
				else if(line.contains("project.ext.pluginClass")){
					System.out.print("Found plugin main class : ");
					pluginClass = line.substring(line.indexOf('"') + 1, line.lastIndexOf('"'));
					System.out.println(pluginClass);
				}
			}
			
			if(name == null || version == null || provider == null || pluginClass == null)
				return false;
			
			// TODO
			frame.pluginName.setText("Editing - " + name);
			frame.pluginID.setText(name);
			frame.pluginProvider.setText(provider);
			frame.pluginVersion.setText(version);
			frame.pluginClass.setText(pluginClass);
			
			
		} catch (IOException e) {
			return false;
		}
		
		return true;
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
		
		save(frame);
		
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
		if(currentPlugin == null || !currentPlugin.exists())
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
			writer.writeLine("File Stats", frame.folderStats.isSelected());
			writer.writeLine("Recent", strings);
			writer.save();

			String whereItShouldBe = currentPlugin.getAbsolutePath() + File.separator + "build.gradle";
			File file = new File(whereItShouldBe);
			List<String> lines = FileUtils.readLines(file, Charset.defaultCharset());
			int[] indexes = new int[4];

			int i = 0;
			for(String line : lines){
				if(line.contains("project.ext.pluginName")){
					indexes[0] = i;
				}else if(line.contains("project.ext.pluginVersion")){
					indexes[1] = i;
				}
				else if(line.contains("project.ext.pluginProvider")){
					indexes[2] = i;
				}
				else if(line.contains("project.ext.pluginClass")){
					indexes[3] = i;
				}
				i++;
			}
			
			lines.set(indexes[0], "project.ext.pluginName = \"" + frame.pluginID.getText().trim() + '"');
			lines.set(indexes[1], "project.ext.pluginVersion = \"" + frame.pluginVersion.getText().trim() + '"');
			lines.set(indexes[2], "project.ext.pluginProvider = \"" + frame.pluginProvider.getText().trim() + '"');
			lines.set(indexes[3], "project.ext.pluginClass = \"" + frame.pluginClass.getText().trim() + '"');

			FileUtils.writeLines(file, lines, false);
			
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
