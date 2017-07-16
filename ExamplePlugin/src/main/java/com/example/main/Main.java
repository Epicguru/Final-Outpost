package com.example.main;

import com.badlogic.gdx.math.Vector3;

import co.uk.epicguru.API.plugins.FinalOutpostPlugin;
import co.uk.epicguru.configs.Config;
import co.uk.epicguru.logging.Log;
import ro.fortsoft.pf4j.PluginException;
import ro.fortsoft.pf4j.PluginWrapper;

/**
 * This is an example of a functional plugin.
 * It is quite well annotated and has the current basic function that a plugin needs.
 * @author James Billy
 */
public class Main extends FinalOutpostPlugin {

	// Technically optional, but a very good idea.
	public static Main INSTANCE;
	
	// Optional, used when debugging.
	public static final String TAG = "Example Plugin";
	
	public Main(PluginWrapper wrapper) {
		super(wrapper, "Example Plugin by James", "0.0.0");
		// First String is the name of this plugin that the user sees.
		// The second one is the user-friendly version of this plugin. 
		// You can type whatever you want in either of these fields.
		
		// Optional, but useful.
		INSTANCE = this;
	}

	@Override
	public void start() throws PluginException {
		// Called as soon as the program starts and the plugin is loaded.
		// Do not load any content or attempt to interact with any other plugins here.
		Log.info(TAG, "Hello, world!");
		
		/* However we do need to define any configuration files (configs) here.
		 * We do this by using newConfig()
		 * We should save the value returned for later use.
		 * All configs created with newConfig() are automatically saved and handled.
		 */		
		Config config = newConfig("ExampleConfig");
		
		// Add a field to the config. Note that all fields must have an associated JLineIO parser.
		// See JLineIO for more info.
		config.add("MyVariable", "Hello world!");
		config.add("MyNumber", 123);
		config.add("MyVector", new Vector3(0, 1, 2));
		config.add("MyString", "Something \n Cool <> &&");
	}
	
	public boolean config(Config config){
		
		/*
		 * This is where the config files are loaded.
		 * Config files are loaded before init() is called but after start().
		 */
		
		// Since we can save many config files per plugin, we need to check which one this is.
		if(config.is("ExampleConfig")){
			
			// Read the values.
			String a = 	(String) config.get("MyVariable");
			int b = 	(int) config.get("MyNumber");
			Vector3 c = (Vector3) config.get("MyVector");
			
			// Do something with them.
			Log.info(TAG, "MyVaraible : " + a);
			Log.info(TAG, "MyNumber : " + b);
			Log.info(TAG, "MyVector : " + c);
		}
		
		// Return true to indicate that the config was managed.
		return true;
	}
	
	public void init(){
		// Called after content is loaded and after configs are loaded.
		// Use this for any 1-time initialisation that this plugin requires to function.
		
		// Do not try to interact with other plugins here unless you know that they do not require any init.
	}
	
	public void postInit(){
		// Called after init()
		// You can safely interact with other plugin or whith the main program here.
	}

	@Override
	public void stop() throws PluginException {
		// Called when the program exits.
		Log.info(TAG, "Bye bye...");
	}
}
