package co.uk.epicguru.API.plugins.assets;

import java.io.File;

import com.badlogic.gdx.assets.AssetManager;

import co.uk.epicguru.API.plugins.FinalOutpostPlugin;
import co.uk.epicguru.API.plugins.PluginsLoader;
import co.uk.epicguru.logging.Log;
import co.uk.epicguru.main.FOE;
import ro.fortsoft.pf4j.Plugin;

public class PluginAssetLoader extends AssetManager {
	
	private static final String TAG = "PluginAssetLoader";
	private File root;
	
	/**
	 * Assumes that plugins assets have been extracted using the PluginLoader.
	 */
	public PluginAssetLoader(){
		root = new File(FOE.gameDirectory + FOE.gamePluginsExtracted);
		
		if(!root.exists()){
			Log.error(TAG, "Root extracted folder does not exist! " + root.getAbsolutePath());
			return;
		}
		
		Log.info(TAG, "Found " + root.list().length + " files or folders in extracted folder.");
	}
	
	/**
	 * Loads all assets in all plugins of type 'type'.
	 * @param pluginLoader The pluginLoader to get all plugins from.
	 * @param type The type of assets to load.
	 */
	public void loadAllAssets(PluginsLoader pluginLoader, AssetLoadType type){
		
		int start = getQueuedAssets();
		Log.info(TAG, "Loading all assets of type " + type.toString() + " in all plugins.");
		Log.info(TAG, "There are currently " + start + " assets queued to be loaded.");
		
		int temp = start;
		
		for(FinalOutpostPlugin plugin : pluginLoader.getAllPlugins()){
			temp = getQueuedAssets();
			plugin.loadAssets(this, type);
			Log.info(TAG, "Plugin '" + plugin.getWrapper().getPluginId() + "' requested to load " + (getQueuedAssets() - temp) + " assets.");
		}
	}
	
	/**
	 * Gives the contentLoaded message to all plugins.
	 */
	public void passMessage(PluginsLoader pluginLoader){
		for(Plugin plugin : pluginLoader.getAllPlugins()){
			pluginLoader.getFOPlugin(plugin.getWrapper().getPluginId()).contentLoaded();
		}
	}
}
