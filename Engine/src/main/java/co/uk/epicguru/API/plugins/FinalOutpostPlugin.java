package co.uk.epicguru.API.plugins;

import java.io.File;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import co.uk.epicguru.API.plugins.assets.AssetLoadType;
import co.uk.epicguru.API.plugins.assets.PluginAssetLoader;
import co.uk.epicguru.configs.Config;
import co.uk.epicguru.configs.ConfigLoader;
import co.uk.epicguru.main.FOE;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

public abstract class FinalOutpostPlugin extends Plugin{

	private String displayName, displayVersion;
	private ArrayList<Config> configs = new ArrayList<>();
	private String assetsFolder;
	
	/**
	 * The base class that all plugins should implement as a main class.
	 * Use start to create and save configuration files.
	 * <p>
	 * Things that ARE auto loaded : 
	 * <ul>
	 * <li>-All JLineIO parsers.
	 * <li>-Kryonet messages that inherit from BaseMessage
	 * </ul>
	 * <p>
	 * Things that ARE NOT auto loaded (But can be loaded) : 
	 * <ul>
	 * <li> All classes (MUST - Implement ExtensionPoint or extend Base AND have the @Extension annotation on the class)
	 * </ul>
	 * @param wrapper The wrapper, will be handed to you.
	 * @param displayName The name of this plugin, as users will see it. Make it pretty.
	 * @param displayVersion The version of this plugin, as users will see it. This does not have to follow any conventions.
	 */
	public FinalOutpostPlugin(PluginWrapper wrapper, final String displayName, final String displayVersion) {
		super(wrapper);
		this.displayName = displayName;
		this.displayVersion = displayVersion;
		this.assetsFolder = new File(FOE.gameDirectory + FOE.gamePluginsExtracted + wrapper.getPluginId() + "/assets/").getAbsolutePath() + '\\';
		this.assetsFolder = this.assetsFolder.replace(Gdx.files.getExternalStoragePath(), "");
	}

	/**
	 * Starts a new config. This config will be automatically saved when necessary.
	 */
	public Config newConfig(String name){
		Config config = new Config(name);
		configs.add(config);
		return config;
	}
	
	/**
	 * Gets all local configs.
	 */
	public ArrayList<Config> getRegisteredConfigs(){
		return configs;
	}
	
	/**
	 * Gets a config given its name.
	 */
	public Config getConfig(String name){
		for(Config config : configs){
			if(config.is(name))
				return config;
		}
		return null;
	}
	
	/**
	 * Gets the class through which assets are loaded into memory for use.
	 * Same as <code>FOE.pluginsAssetsLoader;</code>
	 */
	public PluginAssetLoader getAssetLoader(){
		return FOE.pluginsAssetsLoader;
	}
	
	/**
	 * Loads all configs that are saved on the disk.
	 */
	public void loadConfigs(){
		ConfigLoader.loadConfigsFor(getWrapper().getPluginId());
	}
	
	/**
	 * Loads all configs without reading from file. (as in those registered with {@link #newConfig(String)}).
	 * Not recommended as erroneous data may crop up. ONLY USE IF YOU KNOW WHAT YOU ARE DOING.
	 */
	public void loadConfigsLocal(){
		for(Config config : configs){
			config(config);
		}
	}
	
	/**
	 * Saves all configs registered with {@link #newConfig(String)}.
	 */
	public void saveConfigs(){
		for(Config config : configs){
			FOE.loadingSubText = getWrapper().getPluginId() + " : " + config.getName();
			config.save(this);
		}
	}
	
	/**
	 * Gets the display name.
	 */
	public String getDisplayName(){
		return displayName;
	}
	
	/**
	 * Gets the display version.
	 */
	public String getDisplayVersion(){
		return displayVersion;
	}
	
	/**
	 * Called when a config is loaded from disk.
	 * @param config The auto-loaded config.
	 * @return True if the config was loaded properly. False if not managed.
	 */
	public boolean config(Config config){
		return false;
	}
	
	/**
	 * Called when a certain type of assets should be loaded.
	 * <li>
	 * IMPORTANT : Not to be confused with {@link #loadAsset(PluginAssetLoader, String, Class)} which loads an individual asset.
	 * <li>
	 * NOTE : This does not load an asset. It is a plugin callback which by default does nothing.
	 * @param type The type of assets needed to be loaded.
	 * @return True if some assets were loaded.
	 */
	public boolean loadAssets(PluginAssetLoader loader, AssetLoadType type){
		return false;
	}
	
	/**
	 * Called after ALL plugins have been loaded and ALL content has been loaded and ALL configs have been loaded.
	 * This allows for interaction between plugins at start up if required.
	 */
	public void init(){
		
	}
	
	/**
	 * Called after ALL plugins have been loaded and ALL content has been loaded and after ALL configs have been loaded AND after {@link #init()}.
	 * This serves as a second layer to interaction.
	 */
	public void postInit() {

	}
	
	/**
	 * Gets the assets folder for an extracted assets directory.
	 */
	public String getAssetsFolder(){
		return new File(FOE.gameDirectory + FOE.gamePluginsExtracted + getWrapper().getPluginId() + "/assets/").getAbsolutePath();
	}

	/**
	 * Gets an asset that has been loaded using {@link #loadAsset(PluginAssetLoader, String, Class)};
	 * @param name The name of the asset. For example to get "thing/foo.png" do <code>getAsset("thing/foo.png", Texture.class)</code>
	 * @param type The type of asset to load.
	 * @return A new instance of the already loaded asset.
	 * @see {@link #loadAsset(PluginAssetLoader, String, Class)}
	 */
	public <T> T getAsset(String name, Class<T> type){
		return getAssetLoader().get(this.assetsFolder.replaceAll("\\\\", "/") + name.replaceAll("\\\\", "/"), type);
	}
	
	/**
	 * Loads an asset for the whole program, that is able to be used my this plugin.
	 * <li>
	 * NOTE : This DOES NOT get the asset to you, it only loads it ready for use. This should be done in the {@link #loadAssets(PluginAssetLoader, AssetLoadType)}.
	 * <li>
	 * IMPORTANT : Not to be confused with {@link #loadAssets(PluginAssetLoader, AssetLoadType)} which is a plugin callback.
	 * @see {@link #getAsset(String, Class)} for getting a loaded asset.
	 * @param path The path or name of the asset within the ./assets folder.
	 * @param clazz The type of asset.
	 */
	public void loadAsset(String path, Class<?> clazz){
		getAssetLoader().load(assetsFolder + path.replaceAll("/", "\\\\"), clazz);
	}
	
	/**
	 * Used to load plugin assets. To load assets/Thing.png, do getAsset("Thing.png").
	 */
	public FileHandle getFileHandle(String path){
		return new FileHandle(FOE.gameDirectory + FOE.gamePluginsExtracted + getWrapper().getPluginId() + "/assets/" + path);
	}	
}
