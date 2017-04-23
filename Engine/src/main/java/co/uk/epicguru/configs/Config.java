package co.uk.epicguru.configs;

import java.io.File;
import java.util.HashMap;

import co.uk.epicguru.API.Base;
import co.uk.epicguru.API.U;
import co.uk.epicguru.API.plugins.FinalOutpostPlugin;
import co.uk.epicguru.IO.JLIOException;
import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.IO.JLineWriter;
import co.uk.epicguru.main.FOE;

/**
 * A config local to one plugin that is saved and loaded using JLineIO.
 */
public class Config extends Base{
	
	private HashMap<String, ConfigVariable> map = new HashMap<>();
	private String name;
	
	public Config(JLineReader reader, FinalOutpostPlugin plugin){
		
		// Set name
		this.name = U.nameFromPathNoExtension(reader.getFile().getAbsolutePath(), '\\');
		
		// Gets all variables
		reader.readAllLines();
		
		Config oldConfig = plugin.getConfig(this.name);
		HashMap<String, ConfigVariable> oldMap = null;
		if(oldConfig != null){
			oldMap = oldConfig.cloneMap();
		}
		// Add all variables to list.
		for(String key : reader.getLoadedValues().keySet()){
			Object object = null;
			if(oldMap != null && oldMap.containsKey(key)){
				object = oldMap.get(key).getDefaultValue();
			}
			add(key, object);
			set(key, reader.read(key));
		}
		
		if(oldMap == null)
			return;
		for(String oldKey : oldMap.keySet()){
			if(!map.containsKey(oldKey)){
				add(oldKey, oldMap.get(oldKey).getDefaultValue());
				if(oldMap.get(oldKey).getValue() != null){
					set(oldKey, oldMap.get(oldKey).getValue());					
				}else{
					set(oldKey, oldMap.get(oldKey).getDefaultValue());
					print("Variable '" + oldKey + "' was not loaded in file but was designated as one in start(). Created a default value var.");
				}
			}
		}
	}
	
	private HashMap<String, ConfigVariable> cloneMap(){
		HashMap<String, ConfigVariable> newMap = new HashMap<>();
		
		for(String key : this.map.keySet()){
			newMap.put(key, map.get(key).clone());
		}
		
		return newMap;
	}
	
	/**
	 * Gets the name of this config, as specified in the constructor OR as loaded from file.
	 * @return
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Sets all variables to their default values IF the variables are null.
	 */
	public void setDefault(){
		for(ConfigVariable var : map.values()){
			if(var.getValue() == null)
				var.setDefault();
		}
	}
	
	/**
	 * Shorthand for <code>getName().eqauls(name)</code>
	 */
	public boolean is(String name){
		return getName().equals(name);
	}
	
	/**
	 * Creates a new local config called <code>name</code>
	 */
	public Config(String name) { this.name = name; }
	
	/**
	 * Adds a new variable to this config.
	 * The value and default value of all variables must have JLineIO parsers. See JLineIO for more info.
	 * @param key The key (name) of the variable.
	 * @param defaultValue The default value that this variable will have. Use {@link #set(String, Object)} to set the value.
	 * @see {@link #read(String)}, {@link #set(String, Object)}
	 */
	public void add(String key, Object defaultValue){
		if(!map.containsKey(key))
			map.put(key, new ConfigVariable(key, defaultValue));
	}
	
	/**
	 * Sets the real value of a variable. Use this when the variable needs changing, for example
	 * when the user changes something that needs to be saved in the config.
	 * @param key The key of the variable to change.
	 * @param newValue The value that will be set.
	 * @return True if the variable exists.
	 * @see {@link #add(String, Object)}
	 */
	public boolean set(String key, Object newValue){
		ConfigVariable config = map.get(key);
		
		if(config == null)
			return false;
		
		config.setValue(newValue);
		return true;
	}
	
	/**
	 * Reads a variables REAL value.
	 * @param key The key of the variable.
	 */
	public Object read(String key){
		if(map.containsKey(key))
			return map.get(key).getValue();
		else{
			error("Could not find variable of name '" + key + "'! Check spelling...");
			return null;
		}
	}
	
	/**
	 * Gets the underlying HashMap.
	 */
	public HashMap<String, ConfigVariable> getMap(){
		return map;
	}
	
	/**
	 * Gets all keys, made pretty for your precious eyes. :D
	 */
	public String getKeysPretty(){
		StringBuilder str = new StringBuilder();
		for(String key : getMap().keySet()){
			str.append(key);
			str.append('\n');
		}
		return str.toString();
	}
	
	/**
	 * Saves the config to disk.
	 * @param plugin The plugin to save as.
	 */
	public void save(FinalOutpostPlugin plugin){
		save(FOE.gameDirectory + FOE.configsDirectory + plugin.getWrapper().getPluginId() + "/" + getName() + FOE.configsExtension);
	}
	
	/**
	 * Saves the config to disk.
	 * @param path The path of the file to save to.
	 */
	public void save(String path){
		save(new File(path));
	}
	
	/**
	 * Saves the config to disk.
	 * @param file The file to save to.
	 */
	public void save(File file){
		try {
			JLineWriter writer = new JLineWriter(file);
			
			boolean tryRead = false;
			if(file.exists())
				tryRead = true;
			
			JLineReader reader = null;
			if(tryRead){
				try{
					reader = new JLineReader(file);
					reader.readAllLines();
				}catch(JLIOException e2){
					// No debug
					tryRead = false;
				}
			}
			
			for(ConfigVariable var : map.values()){
				Object value = var.getValue();
				if(value == null){
					if(tryRead){
						Object value2 = reader.read(var.getKey());
						writer.writeLine(var.getKey(), value2 == null ? var.getDefaultValue() : value2);	
					}else{						
						writer.writeLine(var.getKey(), var.getDefaultValue());					
					}
				}else{
					writer.writeLine(var.getKey(), var.getValue());					
				}
			}
			
			if(!writer.save()){
				throw new Exception("Failed to write the config file!");
			}
			
		} catch (Exception e){
			error("The writer for config '" + file.getAbsolutePath() + "' could not be created.", e);
		}
	}
	
	/**
	 * Gets the names of all keys registered with {@link #add(String, Object)}.
	 */
	public String[] getKeys(){
		return getKeysPretty().split("\n");
	}
	
	/**
	 * Gets an underlying ConfigVariable given a key.
	 */
	public Object getVariable(String key){
		if(!map.containsKey(key)){
			error("Unable to find value for key '" + key + "' (Loaded " + map.size() + " vars)");
			return null;
		}
		
		ConfigVariable var = map.get(key);
		return var.getValue();
	}
	
}
