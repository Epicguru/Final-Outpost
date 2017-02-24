package co.uk.epicguru.configs;

import java.io.File;

import co.uk.epicguru.API.U;
import co.uk.epicguru.API.plugins.FinalOutpostPlugin;
import co.uk.epicguru.IO.JLIOException;
import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.logging.Log;
import co.uk.epicguru.main.FOE;

public final class ConfigLoader {
	private ConfigLoader() {}
	
	private static final String TAG = "Config Loader";
	private static File root;
	
	/**
	 * Loads all configs. Called after the FOE has been loaded.
	 */
	public static void loadConfigs(){
		String dir = FOE.gameDirectory + FOE.configsDirectory;
		Log.info(TAG, "Starting config load from -" + dir);
		
		root = new File(dir);
		
		// Start timer.
		final String timer = "Loading configs...";
		U.startTimer(timer);
		
		// Load all
		checkRoot();	
		loadAllConfigs();
		
		// End timer
		Log.info(TAG, "Total time : " + U.endTimer(timer) + " seconds.");
	}
	
	/**
	 * Loads configs for specified plugin.
	 * @param modID The plugin ID to load for. The config sub-folder must be named this.
	 */
	public static void loadConfigsFor(String pluginID){
		
		FOE.loadingSubText = pluginID;
		
		// Start timer
		U.startTimer(pluginID + " - Loading config");
		
		// Get plugin (counts as check)
		FinalOutpostPlugin plugin = FOE.pluginsLoader.getFOPlugin(pluginID);
		if(plugin == null){
			Log.error(TAG, "Could not load plugins for '" + pluginID + "' because the plugin is not loaded.");
			return;
		}
		
		// Get files
		File[] files = U.getFilesWithEnding(new File(root.getAbsoluteFile() + "\\" + pluginID), FOE.configsExtension);
		Log.info(TAG, '[' + pluginID + "] Found " + files.length + " configs.");
		
		// Load configs into plugin.
		for(File file : files){
			try {
				JLineReader reader = new JLineReader(file);
				Config config = new Config(reader);
				boolean worked = plugin.config(config);
				if(!worked){
					Log.error(TAG, "The plugin '" + pluginID + "' did not manage to process the config " + file.getName());
				}
			} catch (JLIOException e) {
				Log.error(TAG, "Could not open reader!", e);
				continue;
			}
		}
		
		// End timer
		Log.info(TAG, '[' + pluginID + "] Took " + U.endTimer(pluginID + " - Loading config") + " seconds.");
	}
	
	public static void loadAllConfigs(){
		
		if(root == null){
			Log.error(TAG, "Root is null!");
			return;
		}
		
		for(File file : root.listFiles()){
			if(file.isDirectory()){
				loadConfigsFor(file.getName());
			}else{
				Log.debug(TAG, "Unknown loose file in config folder -" + file.getName());
			}
		}	
		
		// A little cleanup
		System.gc();
	}
	
	/**
	 * Sets up and/or checks the root config directory.
	 */
	public static void checkRoot(){
		if(!root.exists()){
			if(root.mkdirs()){
				Log.info(TAG, "Created the config directory.");
			}else{
				Log.error(TAG, "Config root folder creation failed!");
				System.exit(-1);
			}
		}else{
			if(!root.isDirectory()){
				// Why ?!?! haha
				if(!root.delete()){
					Log.error(TAG, "The root config directory is NOT a direcotory and furthermore cannot be deleted!");
					System.exit(-1);
				}else{
					if(root.mkdirs()){
						Log.info(TAG, "Created the config directory. (Deleted file and created directory)");
					}else{
						Log.error(TAG, "Config root folder creation failed! (Deleted file)");
						System.exit(-1);
					}
				}
			}else{
				Log.debug(TAG, "Found config folder. It contains " + root.listFiles().length + " files and folders.");
			}
		}
	}
	
}
