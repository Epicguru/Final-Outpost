package co.uk.epicguru.languages.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;

import co.uk.epicguru.API.Base;

/**
 * All lines starting with # are ignored. Use them as comments.
 * @author James Billy
 */
public class LanguagePack extends Base implements Disposable{

	private HashMap<String, String> values = new HashMap<String, String>();
	private String name;
	
	public LanguagePack(String name, FileHandle file){
		this.name = name;
		
		// Read whole file and split into lines
		String[] lines = file.readString().split("\n");		
		
		// Pass to internal method
		this.set(lines);
	}
	
	public LanguagePack(String name, File file){
		this.name = name;
		
		try {
			// Read lines
			List<String> lines = FileUtils.readLines(file, Charset.defaultCharset());
			
			// Get array and pass it to internal method
			String[] array = lines.toArray(new String[0]);
			this.set(array);
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public LanguagePack(String name, LanguagePack other){
		this.name = name;
		
		this.inject(other);
	}
	
	public LanguagePack(String name, String[] lines){
		this.name = name;
		this.set(lines);
	}
	
	public String getName(){
		return this.name;
	}
	
	public void clear(){
		this.values.clear();
	}
	
	public void set(String[] lines){
		if(lines == null)
			throw new IllegalArgumentException("Language data cannot be null!");
		
		this.clear();
		for(String s : lines){
			// There are two parts, one before the '-' character
			// which is the name, and the part after is the value. Both are trimmed.
			
			if(s == null)
				continue;			
			if(s.trim().startsWith("#"))
				continue;
			if(s.trim().isEmpty())
				continue;			
			
			if(s.contains("-") && s.trim().length() > 1){
				
				int index = s.indexOf('-');
				String name = s.substring(0, index);
				name = name.trim();
				String value = s.substring(index + 1);
				value = value.trim();
				
				this.inject(name, value);
				
			}else{
				error("Line '" + s + "' is an invalid language pack line! It has been ignored.");
			}
		}
	}
	
	public void inject(LanguagePack pack){
		for(String key : pack.getHashMap().keySet()){
			String value = pack.getHashMap().get(key);
			
			this.inject(key, value);
		}
	}
	
	public void inject(String name, String value){
		this.values.put(name, value);
	}
	
	public LanguagePack merge(String newName, LanguagePack other){
		LanguagePack newPack = this.clone();
		newPack.inject(other);
		
		return newPack;
	}
	
	public LanguagePack clone(){
		return this.clone(this.getName());
	}
	
	public LanguagePack clone(String newName){
		return new LanguagePack(newName, this);
	}
	
	public HashMap<String, String> getHashMap(){
		return this.values;
	}
	
	public int valueCount(){
		return this.getHashMap().size();
	}
	
	public String get(final String name){
		return this.values.get(name);
	}
	
	public String toString(){
		return this.getName() + ", " + this.valueCount() + " values.";
	}

	public String getWholeLang(){
		StringBuilder str = new StringBuilder();
		
		for(String key : this.getHashMap().keySet()){
			str.append(key);
			str.append("- ");
			str.append(this.getHashMap().get(key));
			str.append('\n');
		}
		
		return str.toString();
	}
	
	public void dispose(){
		this.values.clear();
		this.values = null;
	}
}
