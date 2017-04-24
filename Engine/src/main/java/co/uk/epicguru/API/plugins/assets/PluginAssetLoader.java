package co.uk.epicguru.API.plugins.assets;

import java.io.File;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;

import co.uk.epicguru.API.plugins.FinalOutpostPlugin;
import co.uk.epicguru.API.plugins.PluginsLoader;
import co.uk.epicguru.languages.Lan;
import co.uk.epicguru.logging.Log;
import co.uk.epicguru.main.FOE;

public class PluginAssetLoader extends AssetManager {
	
	private static final String TAG = "PluginAssetLoader";
	private File root;
	
	/**
	 * Assumes that plugins assets have been extracted using the PluginLoader.
	 */
	public PluginAssetLoader(){
		super(new ExternalFileHandleResolver());
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
	
	public void loadAllLanguages(PluginsLoader pluginLoader){
		Log.info(TAG, "Getting all language packs from all plugins...");
		Lan.clear();
		int oldCount = 0;
		for(FinalOutpostPlugin plugin : pluginLoader.getAllPlugins()){
			
			// Responsive, yay!
			FOE.loadingSubText = plugin.getDisplayName();
			
			if(!plugin.loadLanguages()){
				Log.error(TAG, "Plugin '" + plugin.getWrapper().getPluginId() + "' did NOT handle language packs. Oh well.");
				if(Lan.getLangCount() > oldCount){
					Log.error(TAG, "Ooops, I take that back. They added one or more language pack(s). Huh. Odd.");
				}
			}
			
			Log.info(TAG, plugin.getWrapper().getPluginId() + " added " + (Lan.getLangCount() - oldCount + " language packs."));
			
			oldCount = Lan.getLangCount();
		}
		Log.info(TAG, "Done, loaded total of " + Lan.getLangCount() + " languages.");
	}

	/**
	 * Packs all textures for all plugins.
	 */
	public void packAllTextures(PluginsLoader loader){
		Log.info(TAG, "Packing textures for all plugins...");
		
		for(FinalOutpostPlugin plugin : loader.getAllPlugins()){
			if(plugin.needsToPack()){
				FOE.loadingSubText = "Packing textures for " + plugin.getDisplayName();
				plugin.packTextures();
				Log.info(TAG, "Packed all textures for '" + plugin.getWrapper().getPluginId() + '\'');				
			}else{
				Log.info(TAG, "Skipping packing for '" + plugin.getWrapper().getPluginId() + '\'');				
				FOE.loadingSubText = "Skipping " + plugin.getDisplayName();
			}
		}
	}
}
