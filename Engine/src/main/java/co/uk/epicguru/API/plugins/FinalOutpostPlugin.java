package co.uk.epicguru.API.plugins;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

import co.uk.epicguru.API.plugins.assets.AssetLoadType;
import co.uk.epicguru.API.plugins.assets.PluginAssetLoader;
import co.uk.epicguru.configs.Config;
import co.uk.epicguru.main.FOE;
import ro.fortsoft.pf4j.PluginWrapper;

/**
 * Is the upper end of plugin development, contains only methods that are intended to be overridden by design.
 * @author James Billy
 *
 */
public abstract class FinalOutpostPlugin extends PluginBackend{
	
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
	 * Called AFTER {@link #postInit()} and this is where you should load
	 * {@link LanguagePack}s. Use the {@link Lan} class to put the language packs in.
	 * @return
	 */
	public boolean loadLanguages(){
		return false;
	}
	
	/**
	 * Called after ALL plugins have been loaded and INIT_CORE content has been loaded and ALL configs have been loaded.
	 * Here is where you should define, check and maintain inputs using {@link #addInput(String, int)} and other methods.
	 * This allows for interaction between plugins at start up if required.
	 */
	public void init(){
		
	}
	
	/**
	 * Called after ALL plugins have been loaded and INIT_CORE content has been loaded and after ALL configs have been loaded AND after {@link #init()}.
	 * This serves as a second layer to interaction. Adding inputs is also valid here and after this.
	 */
	public void postInit() {

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
		settings.maxWidth = 2048;
		settings.maxHeight = 2048;
		settings.fast = true;
		
		return settings;
	}	

	/**
	 * Called at potentially any time, when the JSON serialization system creates the serializer.
	 * You can add 'tags' for classes to shorten class names. For example, JSON serializes the class
	 * <code>Integer</code> as "java.lang.Integer". This is inefficient and ugly, so instead here you could do:
	 * <code>tag.add(Integer.class, "Int")</code>. You can do this for any class you want, but note that the
	 * basic classes such as <code>String, Boolean, Float, Integer</code> have already been done.
	 * <li>
	 * IMPORTANT: Once you add a class tag, DO NOT REMOVE IT OR MODIFY IT. This could lead to crashes in serialization.
	 * @param tag A functional interface that has the <code>add</code> method.
	 */
	public void addClassTags(AddTag tag){
		
	}
}
