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
				if(oldMap.get(oldKey).getValue() != null)
					set(oldKey, oldMap.get(oldKey).getValue());
				else
					set(oldKey, oldMap.get(oldKey).getDefaultValue());
				print("Variable '" + oldKey + "' was not loaded in file but was designated as one in start(). Created a default value var.");
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
	
	public String getName(){
		return this.name;
	}
	
	public void setDefault(){
		for(ConfigVariable var : map.values()){
			if(var.getValue() == null)
				var.setDefault();
		}
	}
	
	public boolean is(String name){
		return getName().equals(name);
	}
	
	public Config(String name) { this.name = name; }
	
	public void add(String key, Object defaultValue){
		map.put(key, new ConfigVariable(key, defaultValue));
	}
	
	public boolean set(String key, Object newValue){
		ConfigVariable config = map.get(key);
		if(config == null)
			return false;
		
		config.setValue(newValue);
		return true;
	}
	
	public Object read(String key){
		return map.get(key).getValue();
	}
	
	public HashMap<String, ConfigVariable> getMap(){
		return map;
	}
	
	public String getKeysPretty(){
		StringBuilder str = new StringBuilder();
		for(String key : getMap().keySet()){
			str.append(key);
			str.append('\n');
		}
		return str.toString();
	}
	
	public void save(FinalOutpostPlugin plugin){
		save(FOE.gameDirectory + FOE.configsDirectory + plugin.getWrapper().getPluginId() + "/" + getName() + FOE.configsExtension);
	}
	
	public void save(String path){
		save(new File(path));
	}
	
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
	
	public String[] getKeys(){
		return getKeysPretty().split("\n");
	}
	
	public Object getValue(String key){
		if(!map.containsKey(key)){
			error("Unable to find value for key '" + key + "' (Loaded " + map.size() + " vars)");
			return null;
		}
		
		ConfigVariable var = map.get(key);
		return var.getValue();
	}
	
}
