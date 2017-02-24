package co.uk.epicguru.API.plugins;

import java.io.File;

import com.badlogic.gdx.files.FileHandle;

import co.uk.epicguru.main.FOE;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

public abstract class FinalOutpostPlugin extends Plugin{

	private String displayName, displayVersion;
	
	/**
	 * The base class that all plugins should implement as a main class.
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
