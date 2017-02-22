package co.uk.epicguru.API;

import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

public abstract class FinalOutpostPlugin extends Plugin{

	private String displayName, displayVersion;
	
	/**
	 * The base class that all plugins should implement as a main class.
	 * @param wrapper The wrapper, will be handed to you.
	 * @param displayName The name of this plugin, as users will see it. Make it pretty.
	 * @param displayVersion The version of this plugin, as users will see it. This does not have to follow any conventions.
	 */
	public FinalOutpostPlugin(PluginWrapper wrapper, final String displayName, final String displayVersion) {
		super(wrapper);
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
}
