package co.uk.epicguru.configs;

import java.util.HashMap;

import co.uk.epicguru.API.Base;
import co.uk.epicguru.IO.JLineReader;

public class Config extends Base{
	
	private HashMap<String, ConfigVariable> map = new HashMap<>();
	
	public Config(JLineReader reader){
		
		// Gets all variables
		reader.readAllLines();
		
		// add all variables to list.
		for(String key : reader.getLoadedValues().keySet()){
			add(key, reader.read(key));
		}
	}
	
	public void add(String key, Object defaultValue){
		map.put(key, new ConfigVariable(key, defaultValue));
	}
	
	public boolean setValue(String key, Object newValue){
		ConfigVariable config = map.get(key);
		if(config == null)
			return false;
		
		config.setValue(newValue);
		return true;
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
