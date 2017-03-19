package co.uk.epicguru.API.plugins;

import java.io.File;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

import co.uk.epicguru.API.PluginRuntimeException;
import co.uk.epicguru.API.plugins.assets.AssetLoadType;
import co.uk.epicguru.API.plugins.assets.PluginAssetLoader;
import co.uk.epicguru.configs.Config;
import co.uk.epicguru.configs.ConfigLoader;
import co.uk.epicguru.input.Input;
import co.uk.epicguru.logging.Log;
import co.uk.epicguru.main.FOE;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

public abstract class FinalOutpostPlugin extends Plugin{

	private String displayName, displayVersion;
	private ArrayList<Config> configs = new ArrayList<>();
	private ArrayList<String> inputNames = new ArrayList<>();
	private String assetsFolder;
	/**
	 * Please do not alter, but if you are interested in what it does then read the name of the variable to discover what it is.
	 */
	public boolean beforeInit = true;
	
	/**
	 * The base class that all plugins should implement as a main class.
	 * Use start to create and save configuration files.
	 * <p>
	 * Things that ARE auto loaded (Assuming @Extension annotation): 
	 * <ul>
	 * <li>All JLineIO parsers.
	 * <li>Kryonet messages that inherit from BaseMessage
	 * <li>Tile Factories and Tiles
	 * </ul>
	 * <p>
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
	 * Here is where you should define, check and maintain inputs using {@link #addInput(String, int)} and other methods.
	 * This allows for interaction between plugins at start up if required.
	 */
	public void init(){
		
	}
	
	/**
	 * Called after ALL plugins have been loaded and ALL content has been loaded and after ALL configs have been loaded AND after {@link #init()}.
	 * This serves as a second layer to interaction. Adding inputs is also valid here and after this.
	 */
	public void postInit() {

	}
	
	/**
	 * Adds a generic input that allows for the user to configure keys.
	 * This does not work for mouse buttons, only keys. Use the 'Keys' gdx class
	 * to get keys from. (For example Keys.SPACE).
	 * @param name The name of the input.
	 * @param key The number of the key to initially bind to.
	 * @see {@link #changeInput(String, int)}, {@link #removeInput(String)}
	 */
	public void addInput(final String name, int key){
		
		if(beforeInit){
			throw new PluginRuntimeException("Cannot add a new input before init() method is called. Please override init() to do this.");
		}
		
		Input.addInput(this, name, key);
		this.inputNames.add(name);
	}
	
	/**
	 * Changes an existing input, as created with {@link #addInput(String, int)}.
	 * @param name The name of the existing input.
	 * @param newKey The new key to bind it to. See Gdx class Keys.
	 */
	public void changeInput(final String name, int newKey){
		if(beforeInit){
			throw new PluginRuntimeException("Cannot change an input before init() method is called. Please override init() to do this.");
		}
		Input.changeInput(this, name, newKey);
	}
	
	/**
	 * Removes a input added by this plugin using {@link #addInput(String, int)}.
	 * @param name The name of the input to remove.
	 */
	public void removeInput(final String name){
		if(beforeInit){
			throw new PluginRuntimeException("Cannot remove an input before init() method is called. Please override init() to do this.");
		}
		Input.removeInput(this, name);
		this.inputNames.remove(name);
	}
	
	/**
	 * Returns true if the key bound to the specified input is currently pressed on the keyboard.
	 * @param name The name of the input as given in {@link #addInput(String, int)}.
	 * @return True if currently held down, false if not.
	 */
	public boolean isInputDown(final String name){
		return Input.isInputDown(this, name);
	}
	
	/**
	 * Returns true if the key bound to the specified input is currently pressed AND was NOT pressed last frame on the keyboard.
	 * @param name The name of the input as given in {@link #addInput(String, int)}.
	 * @return True if currently just pressed (in terms of frames), false if not.
	 */
	public boolean isInputJustDown(final String name){
		return Input.isInputJustDown(this, name);
	}
	
	/**
	 * Gets the number of the key that the specified input is bound to. This can be changed by {@link #changeInput(String, int)}
	 * or by the user.
	 * @param name The name of the plugin as specified in {@link #addInput(String, int)}.
	 * @return The number (code) of the key mapped to the input, or -1 if the input does not exist.
	 */
	public int getInputCode(final String name){
		return Input.getInputCode(this, name);
	}
	
	/**
	 * Gets the (sort of) user friendly name of the key bound to the specified input.
	 * @param name The name of the input as specified in {@link #addInput(String, int)}.
	 * @return The name of the input, or null if the input does not exist.
	 */
	public String getInputString(final String name){
		return Input.getInputString(this, name);
	}
	
	/**
	 * Gets an array-list of all inputs added and removed through {@link #addInput(String, int)} and {@link #removeInput(String)}.
	 */
	public ArrayList<String> getInputNames(){
		return this.inputNames;
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
	 * A call to {@link #packTextures()} will be triggered if this returns true.
	 */
	public boolean needsToPack(){
		return !new File(new File(Gdx.files.getExternalStoragePath() + this.assetsFolder).getParentFile().getAbsolutePath() + "\\Packed").exists();
	}
	
	/**
	 * Gets the settings used when packing all the textures for this plugin.
	 */
	public Settings getPackerSettings(){
		Settings settings = new Settings();
		
		// Override to manipulate settings.
		settings.maxWidth = (int) Math.pow(2, 16);
		settings.maxHeight = (int) Math.pow(2, 16);
		
		return settings;
	}
	
	/**
	 * Called only once when all textures are packed.
	 */
	public void packTextures(){
		Log.info(this.getDisplayName(), "Packing textures using default implementation");
		
		if(!new File(Gdx.files.getExternalStoragePath() + this.assetsFolder).exists())
			return;
		
		String path = Gdx.files.getExternalStoragePath() + this.assetsFolder;
		
		TexturePacker.process(getPackerSettings(), path, new File(path).getParentFile().getAbsolutePath() + "/Packed", "Textures.atlas");
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
