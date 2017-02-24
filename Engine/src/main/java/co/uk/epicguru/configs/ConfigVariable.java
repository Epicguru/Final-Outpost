package co.uk.epicguru.configs;

import co.uk.epicguru.API.Base;

public class ConfigVariable extends Base implements Cloneable{

	private String key;
	private Object value;
	private Object defaultValue;
	
	public ConfigVariable(String key, Object defaultValue){
		this.key = key;
		this.defaultValue = defaultValue;
	}
	
	public ConfigVariable clone(){
		return new ConfigVariable(this.getKey(), this.getDefaultValue()).setValue(getValue());
	}
	
	public void setDefault(){
		this.value = defaultValue;
	}
	
	public ConfigVariable setValue(Object value){
		this.value = value;
		return this;
	}
	
	public Object getDefaultValue(){
		return defaultValue;
	}
	
	public Object getValue(){
		return value;
	}
	
	public String getKey(){
		return key;
	}
	
	public String toString(){
		return key;
	}	
}
