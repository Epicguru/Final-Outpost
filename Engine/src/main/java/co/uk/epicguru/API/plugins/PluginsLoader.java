package co.uk.epicguru.API.plugins;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.badlogic.gdx.utils.Disposable;

import co.uk.epicguru.logging.Log;
import co.uk.epicguru.main.FOE;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import ro.fortsoft.pf4j.DefaultPluginManager;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginClassLoader;
import ro.fortsoft.pf4j.PluginWrapper;

public final class PluginsLoader extends DefaultPluginManager implements Disposable{

	public static final String TAG = "Plugin Loader";
	private static FinalOutpostPlugin[] _plugins;
	private static ArrayList<PluginWrapper> errorPlugins;
	private ZipFile zip;

	public PluginsLoader(){
		
	}
	
	public static void setupDirectory(String directory){
		System.setProperty("pf4j.pluginsDir", new File(directory).getAbsolutePath());
		Log.info(TAG, "Plugin directory is now " + System.getProperty("pf4j.pluginsDir"));
		new File(directory).mkdirs();
	}

	/**
	 * Starts all loaded and active plugins.
	 */
	public void startPlugins() {
		super.startPlugins();

		errorPlugins = new ArrayList<>();
		ArrayList<FinalOutpostPlugin> pluginsTemp = new ArrayList<>();

		for(PluginWrapper plugin : super.getResolvedPlugins()){
			Plugin pluginLoaded = plugin.getPlugin();

			// Check to see if plugin is valid.
			if(checkPlugin(plugin)){
				pluginsTemp.add((FinalOutpostPlugin)pluginLoaded);				
			}
		}
		_plugins = new FinalOutpostPlugin[pluginsTemp.size()];
		_plugins = pluginsTemp.toArray(_plugins);
		
		Log.info(TAG, "Loaded and resolved plugins - ");
		for(FinalOutpostPlugin plugin : _plugins){
			Log.info(TAG, "  -[" + plugin.getWrapper().getPluginId() + "] " + plugin.getDisplayName() + " v" + plugin.getDisplayVersion() + " for game version " + plugin.getWrapper().getDescriptor().getVersion().toString());
		}
	}

	/**
	 * Does various checks to see if a potential plugin is valid.
	 */
	private boolean checkPlugin(PluginWrapper plugin){
		Plugin mainClass = plugin.getPlugin();

		// Main class null
		if(mainClass == null){
			Log.error(TAG, "Plugin main class for plugin '" + plugin.getPluginId() + "' was null!");
			errorPlugins.add(plugin);
			return false;
		}

		// Main class not subclass of FinalOutpostPlugin
		if(!(mainClass instanceof FinalOutpostPlugin)){
			Log.error(TAG, "Plugin main class for plugin '" + plugin.getPluginId() + "' was NOT a sublass of FinalOutpostPlugin!");
			errorPlugins.add(plugin);
			return false;
		}
		
		return true;
	}

	/**
	 * Cleans up the plugin directory of loose plugin folder that are left from where this plugin loader extracted them.
	 */
	public static  void cleanDirectory(){
		Log.info(TAG, "Cleaing plugin directory");
		for(File file : new File(FOE.gameDirectory + FOE.pluginsDirectory).listFiles()){
			if(file.isFile())
				continue;
			try {
				Log.info(TAG, "Deleting : " + file.getName());
				FileUtils.forceDeleteOnExit(file);
			} catch (IOException e) {
				Log.error(TAG, "Unable to delete file : " + file.getAbsolutePath(), e);
			}
		}
	}
	
	/**
	 * Stops all plugins.
	 */
	public void stopPlugins(){
		super.stopPlugins();
		
		_plugins = null;
		errorPlugins = null;
	}
	
	/**
	 * Saves all registered configs for all plugins.
	 * @see {@link #getAllPlugins()}.
	 */
	public void saveAllConfigs(){
		Log.info(TAG, "Saving all configs...");
		for(FinalOutpostPlugin plugin : getAllPlugins()){
			plugin.saveConfigs();
		}
	}
	
	/**
	 * Extracts the assets for all resolved plugins.
	 * @see {@link #getAllPlugins()}
	 */
	public void extractAllAssets(){
		for(Plugin plugin : getAllPlugins()){
			extractAssetsFor(plugin.getWrapper().getPluginId());
		}
	}
	
	/**
	 * Calls init() on all loaded and resolved plugins.
	 */
	public void initAllPlugins(){
		for(PluginWrapper plugin : getPlugins()){
			FOE.loadingSubText = plugin.getPluginId();
			getFOPlugin(plugin.getPluginId()).init();
		}
	}
	
	/**
	 * Calls postInit() on all loaded and resolved plugins.
	 */
	public void postInitAllPlugins(){
		for(PluginWrapper plugin : getPlugins()){
			FOE.loadingSubText = plugin.getPluginId();
			getFOPlugin(plugin.getPluginId()).postInit();
		}
	}
	
	/**
	 * Checks to see if the plugin has been loaded successfuly.
	 * @param pluginID The ID of the plugin, as in plugin.getWrapper().getPluginId().
	 */
	public boolean isPluginGood(String pluginID){
		for(FinalOutpostPlugin plugin : getAllPlugins()){
			if(plugin.getWrapper().getPluginId().equals(pluginID))
				return true;
		}
		return false;
	}
	
	/**
	 * Gets a FinalOutpost plugin. The plugin will not be found if it was not loaded correctly.
	 * @param pluginID The ID of the plugin, as in plugin.getWrapper().getPluginId().
	 * @return The FinalOutpostPlugin class of the plugin.
	 */
	public FinalOutpostPlugin getFOPlugin(String pluginID){
		if(getPlugin(pluginID) != null)
			return (FinalOutpostPlugin) getPlugin(pluginID).getPlugin();
		else
			return null;
	}

	/**
	 * Unpacks all assets for a given plugin. The plugin must be valid.
	 */
	public void extractAssetsFor(String pluginID){
		
		// Make sure plugin is valid
		if(!isPluginGood(pluginID))
			return;

		FinalOutpostPlugin plugin = (FinalOutpostPlugin) super.getPlugin(pluginID).getPlugin();
		File root = new File(FOE.gameDirectory + FOE.pluginsDirectory + plugin.getWrapper().getPluginPath() + ".zip");
		final String starter = pluginID + '\\';
		File extractionPluginFolder = new File(FOE.gameDirectory + FOE.gamePluginsExtracted + starter);
		
		// Log.debug(TAG, "Looking at " + root.getAbsolutePath()); // Too slow
		
		// Clean extraction directory 
		try {
			FileUtils.deleteDirectory(extractionPluginFolder);
		} catch (IOException e) {
			Log.error(TAG, "ERROR!", e);
		}
		//Log.debug(TAG, "Deleted " + extractionPluginFolder.getAbsolutePath() + "..."); // Too slow
		
		try {
			ZipFile zip = new ZipFile(root);
			this.zip = zip;
			
			FileHeader[] headers = getHeaders(zip, "assets/");
			Log.info(TAG, '[' + pluginID + ']' + "Found " + headers.length + " assets.");
			
			for(FileHeader file : headers){
				zip.extractFile(file, extractionPluginFolder.getAbsolutePath());
			}
			
			this.zip = null;
			
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets all file headers from within a zip file that start with the string 'start'.
	 * @param file The ZipFile to search.
	 * @param start The start of the path.
	 * @return An array of file headers.
	 */
	private FileHeader[] getHeaders(ZipFile file, String start){
		ArrayList<FileHeader> files = new ArrayList<>();
		try {
			for(Object o : file.getFileHeaders()){
				FileHeader header = (FileHeader)o;
				if(header.isDirectory())
					continue;
				if(header.getFileName().startsWith(start)){
					files.add(header);
				}
			}
		} catch (Exception e){
			Log.error(TAG, "Failed reading files from zip whilst looking for '" + start + "' in '" + file.getFile().getName() + "'", e);
			return null;
		}
		
		FileHeader[] files2 = new FileHeader[files.size()];
		files2 = files.toArray(files2);
		return files2;
	}
	
	/**
	 * Disposes all assets loaded by this plugin manager.
	 */
	public void dispose(){
		_plugins = null;
		errorPlugins = null;
		for(PluginClassLoader clazz : super.pluginClassLoaders.values()){
			clazz.dispose();
		}
		super.pluginClassLoaders = null;
		zip = null;
	}
	
	/**
	 * Only for debug purposes.
	 */
	public ZipFile getCurrentZip(){
		return this.zip;
	}
	
	/**
	 * Gets all resolved and running plugins.
	 */
	public FinalOutpostPlugin[] getAllPlugins(){
		if(_plugins == null) _plugins = new FinalOutpostPlugin[0];
		return _plugins;
	}
}
