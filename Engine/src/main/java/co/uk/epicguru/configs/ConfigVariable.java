package co.uk.epicguru.configs;

import co.uk.epicguru.API.Base;
import co.uk.epicguru.IO.JLineReader;

public class ConfigVariable extends Base implements Cloneable{

	private String key;
	private Object value;
	private Object defaultValue;
	
	public ConfigVariable(String key, Object defaultValue){
		this.key = key;
		this.defaultValue = defaultValue;
	}
	
	/**
	 * Clones the config variable, including value, default value and key.
	 */
	public ConfigVariable clone(){
		return new ConfigVariable(this.getKey(), this.getDefaultValue()).setValue(getValue());
	}
	
	/**
	 * Assumes that the variable is in the buffer at the time of passing the JLineReader.
	 */
	public void load(JLineReader reader){
		if(reader.isValueLoaded(getKey())){
			setValue(reader.read(getKey()));
		}else{
			setValue(getDefaultValue());
		}
	}
	
	/**
	 * Sets the value to the default value.
	 */
	public void setDefault(){
		this.value = defaultValue;
	}
	
	/**
	 * Sets the value of this variable.
	 * @param value The new value.
	 * @return This object.
	 */
	public ConfigVariable setValue(Object value){
		this.value = value;
		return this;
	}
	
	/**
	 * Gets the default value.
	 */
	public Object getDefaultValue(){
		return defaultValue;
	}
	
	/**
	 * Gets the current REAL value.
	 */
	public Object getValue(){
		return value;
	}
	
	/**
	 * Gets the key (the name) of this variable.
	 */
	public String getKey(){
		return key;
	}
	
	/**
	 * Same as getKey()
	 */
	public String toString(){
		return key;
	}	
}
