package co.uk.epicguru.API.plugins;

import java.io.File;
import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;

import co.uk.epicguru.configs.Config;
import co.uk.epicguru.configs.ConfigLoader;
import co.uk.epicguru.main.FOE;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

public abstract class FinalOutpostPlugin extends Plugin{

	private String displayName, displayVersion;
	private ArrayList<Config> configs = new ArrayList<>();
	
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
	 * Called when the content has been loaded for this plugin.
	 */
	public void contentLoaded(){
		
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
	 * Used to load plugin assets. To load assets/Thing.png, do getAsset("Thing.png").
	 */
	public FileHandle getAsset(String path){
		return new FileHandle(new File(FOE.gameDirectory + FOE.gamePluginsExtracted + getWrapper().getPluginId() + "/assets/" + path).getAbsolutePath());
	}	
}
