package co.uk.epicguru.configs;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import co.uk.epicguru.API.U;
import co.uk.epicguru.API.plugins.FinalOutpostPlugin;
import co.uk.epicguru.IO.Dataset;
import co.uk.epicguru.IO.JIO;
import co.uk.epicguru.IO.NotSerialized;
import co.uk.epicguru.main.FOE;

/**
 * A config local to one plugin that is saved and loaded using the IO system.
 */
public class Config extends Dataset<Object> {
	
	@NotSerialized private String name;	
	@NotSerialized private FinalOutpostPlugin plugin;
	
	public Config() { }
	
	public Config(File file, FinalOutpostPlugin plugin){
		
		this.name = U.nameFromPathNoExtension(file.getAbsolutePath(), '\\');
		this.plugin = plugin;
		
		// Ensure file exists
		if(!file.exists()){
			error("Config file does not exist! (" + file.getAbsolutePath() + ")");
			return;
		}
		
		// Load all the json data
		String contents = null;
		try {
			contents = FileUtils.readFileToString(file, Charset.defaultCharset());
		} catch (IOException e) {
			error("Error reading config file.", e);
			return;
		}

		// Parse from file json into dataset format, and apply to parent.
		Dataset<Object> dataset = JIO.fromJson(contents, Config.class);
		super.set(dataset);	
		
		// Apply to the default config file.
		this.applyToExisting();
	}
	
	private void applyToExisting(){
		
		// Get existing default config file.
		Config existing = this.plugin.getConfig(this.getName());
		
		// Null check
		if(existing == null){ 
			error("Loaded config '" + this.getName() + "', but no default version was found!");
			return;
		}
		
		if(this.plugin == null){
			error("Could not apply config '" + this.getName() + "' to old verison because plugin is null!");
			return;
		}
		
		HashMap<String, Object> newMap = new HashMap<String, Object>();
		
		// Get all the old values, and add new values, replace old ones.
		for(String key : keys()){
			// Key is the loaded keys.
			
			if(existing.contains(key)){
				// If default config contains the key...
				newMap.put(key, this.get(key)); // Put (replace) the loaded version.
			}else{
				// We have a variable that is not in the default config. 
				// Strange, but will be added anyway, with a warning message.
				error("Unexpected config variable loaded - " + key + ": " + this.get(key) + " in loaded config '" + this.getName() + "' of plugin " + (this.plugin == null ? "Null" : this.plugin.getWrapper().getPluginId()));
				newMap.put(key, this.get(key)); // Add new.
			}
		}
		
		// Second pass to ensure all data is present.
		for(String key : existing.keys()){
			if(newMap.containsKey(key))
				continue;
			
			// Does not contain! Add now.
			newMap.put(key, existing.get(key));
			error("Config '" + this.getName() + "' (" + (this.plugin == null ? "Null" : this.plugin.getWrapper().getPluginId()) + ") that is FROM FILE is missing variable '" + key + "' that is currently '" + existing.get(key) + "'. Current value applied.");
		}
		
		// NOTE: This will be the config applied when it is just loaded, but we must update the default and existing config
		// because it is the one that is saved and that will be edited in real-time.
		
		// Apply
		this.set(newMap);
		
		// Apply to plugin
		this.plugin.getConfig(this.getName()).set(newMap);
	}
	
	public HashMap<String, Object> cloneMap(){
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		for(String key : super.keys()){
			data.put(key, super.get(key));
		}
		
		return data;
	}
	
	public String getPath(){
		return FOE.gameDirectory + FOE.configsDirectory + plugin.getWrapper().getPluginId() + "/" + getName() + FOE.configsExtension;
	}
	
	public boolean onDisk(){
		return new File(getPath()).exists();
	}
	
	public boolean is(String other){
		return this.getName().equals(other);
	}
	
	public Config(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void add(String key, Object defaultValue){
		if(!super.contains(key))
			super.put(key, defaultValue);
	}
	
	public void set(String key, Object newObject){
		super.put(key, newObject);
	}
	
	public void save(FinalOutpostPlugin plugin){
		this.plugin = plugin;
		save();
	}
	
	public void save(){
		this.save(new File(getPath()));
	}
	
	public void save(File file){	
		
		String json = JIO.toJson((Dataset<Object>)this, FOE.prettyConfigs);
		
		try {
			FileUtils.write(file, json, Charset.defaultCharset());
		} catch (IOException e) {
			error("Error writing config file!", e);
		}		
	}
}
