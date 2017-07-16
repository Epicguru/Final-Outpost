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
