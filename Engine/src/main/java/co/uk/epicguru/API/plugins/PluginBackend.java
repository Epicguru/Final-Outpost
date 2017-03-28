package co.uk.epicguru.API.plugins;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

import co.uk.epicguru.API.PluginRuntimeException;
import co.uk.epicguru.API.U;
import co.uk.epicguru.API.plugins.assets.AssetLoadType;
import co.uk.epicguru.API.plugins.assets.PluginAssetLoader;
import co.uk.epicguru.configs.Config;
import co.uk.epicguru.configs.ConfigLoader;
import co.uk.epicguru.input.Input;
import co.uk.epicguru.logging.Log;
import co.uk.epicguru.main.FOE;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

/**
 * Contains all methods that are not intended to be overridden, but may be overridden if you want to.
 * @author James Billy
 */
public abstract class PluginBackend extends Plugin {

	private ArrayList<Config> configs = new ArrayList<>();
	protected String displayName;
	protected String displayVersion;
	protected ArrayList<String> inputNames = new ArrayList<>();
	protected String assetsFolder;
	/**
	 * Please do not alter, but if you are interested in what it does then read the name of the variable to discover what it is.
	 */
	public boolean beforeInit = true;

	public PluginBackend(PluginWrapper wrapper) {
		super(wrapper);
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
	 * Loads all configs for this plugin that are saved on the disk.
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
			config.save((FinalOutpostPlugin)this);
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

	public abstract boolean config(Config config);
	public abstract Settings getPackerSettings();

	/**
	 * Adds a generic input that allows for the user to configure keys.
	 * This does not work for mouse buttons, only keys. Use the {@link Keys} class to get keys from.
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
	 * @param newKey The new key to bind it to. See {@link Keys}.
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
	 * Called only once when all textures are packed.
	 */
	public void packTextures(){
		
		boolean collect = false;
		// True -- Collects, Packs (no faster...) but DOES NOT LOAD???
		// False - Works with all tested assets.
		
		if(collect){	
			Log.info(this.getDisplayName(), "Packing textures using default implementation (Collect mode)");
			if(!new File(Gdx.files.getExternalStoragePath() + this.assetsFolder).exists())
				return;
			
			String path = Gdx.files.getExternalStoragePath() + this.assetsFolder;
			String input = new File(path).getParentFile().getAbsolutePath() + "/Collected";
			String output = new File(path).getParentFile().getAbsolutePath() + "/Packed";
			
			File collectedFolder = new File(input);
			if(!collectedFolder.mkdirs())
				return;
			
			File[] files = U.getFilesWithEnding(new File(path), ".png");
			System.out.println(U.prettify(files));
			for(File file : files){
				try {
					FileUtils.copyFile(file, new File(collectedFolder.getAbsolutePath() + "\\" + file.getName()));
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
						
			TexturePacker.process(getPackerSettings(), input, output, "Textures.atlas");
			
		}else{
			Log.info(this.getDisplayName(), "Packing textures using default implementation (Raw mode)");
			if(!new File(Gdx.files.getExternalStoragePath() + this.assetsFolder).exists())
				return;

			String path = Gdx.files.getExternalStoragePath() + this.assetsFolder;

			TexturePacker.process(getPackerSettings(), path, new File(path).getParentFile().getAbsolutePath() + "/Packed", "Textures.atlas");
		}
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
