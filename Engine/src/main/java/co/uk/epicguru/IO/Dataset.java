package co.uk.epicguru.IO;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import co.uk.epicguru.API.Base;

public abstract class Dataset<T> extends Base{

	private HashMap<String, T> data = new HashMap<String, T>();
	
	public boolean contains(String key){
		return this.data.containsKey(key);
	}
	
	public void put(String key, T value){
		data.put(key, value);
	}
	
	public T get(String key){
		return data.get(key);
	}
	
	public Set<String> keys(){
		return this.data.keySet();
	}

	
	public Collection<T> values(){
		return this.data.values();
	}
	
	public HashMap<String, T> getMap(){
		return this.data;
	}
}
