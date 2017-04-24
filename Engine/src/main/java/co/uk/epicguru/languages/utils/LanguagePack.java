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
 * An object that represents a language within the game. For example English, Spanish and French are all language packs.
 * Language Packs are added to {@link Lan} to use them in game. You can load language packs using the {@link LanguagePackAssetLoader}
 * along with the asset loaded, see the example plugin for more info.
 * @author James Billy
 */
public class LanguagePack extends Base implements Disposable{

	private HashMap<String, String> values = new HashMap<String, String>();
	private String name;
	
	/**
	 * Creates a new pack from a file handle. The file must exist and not be null.
	 */
	public LanguagePack(String name, FileHandle file){
		this.name = name;
		
		// Read whole file and split into lines
		String[] lines = file.readString().split("\n");		
		
		// Pass to internal method
		this.set(lines);
	}
	
	/**
	 * Creates a new pack from a file. The file must exist and not be null.
	 */
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
	
	/**
	 * Clones the other language. The name is obviously not cloned.
	 * @param name The name of this new pack.
	 * @param other The other language to clone.
	 */
	public LanguagePack(String name, LanguagePack other){
		this.name = name;
		
		this.inject(other);
	}
	
	/**
	 * Makes a new language pack from a list of lines.
	 */
	public LanguagePack(String name, String[] lines){
		this.name = name;
		this.set(lines);
	}
	
	/**
	 * Gets the name of this language as given in the constructor.
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Removes all text data from this language, leaving it empty but still usable.
	 */
	public void clear(){
		this.values.clear();
	}
	
	/**
	 * Sets all data to the lines. This clears all previous data.
	 */
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
	
	/**
	 * Adds all data from the other language to this one. Does not modify other, and all previous data is kept.
	 * @param pack The other pack to copy all data from.
	 */
	public void inject(LanguagePack pack){
		for(String key : pack.getHashMap().keySet()){
			String value = pack.getHashMap().get(key);
			
			this.inject(key, value);
		}
	}
	
	/**
	 * Adds a key-value to this language.
	 */
	public void inject(String name, String value){
		this.values.put(name, value);
	}
	
	/**
	 * Creates a NEW language by merging the data of this pack and the other one, to create a new pack.
	 * @param newName The name of the combination of these two packs.
	 * @param other The other pack.
	 * @return The new language pack.
	 */
	public LanguagePack merge(String newName, LanguagePack other){
		LanguagePack newPack = this.clone();
		newPack.inject(other);
		
		return newPack;
	}
	
	/**
	 * Creates a clone of this language, with the same data and name. See
	 * {@link #clone(String)} to create a clone with a unique name.
	 */
	public LanguagePack clone(){
		return this.clone(this.getName());
	}
	
	/**
	 * See {@link #clone()}, but this version creates a clone with a custom name.
	 * @param newName
	 * @return
	 */
	public LanguagePack clone(String newName){
		return new LanguagePack(newName, this);
	}
	
	/**
	 * Gets the HashMap that stores key-value data for this language. PLEASE DO NOT MODIFY.
	 */
	public HashMap<String, String> getHashMap(){
		return this.values;
	}
	
	/**
	 * Gets the count of key-value pairs within this language.
	 * @return
	 */
	public int valueCount(){
		return this.getHashMap().size();
	}
	
	/**
	 * Gets the value of a key in this language.
	 * @param name The key, for example 'PLAY GAME BUTTON'.
	 * @return The value, for example 'Jugar' (for Spanish language pack).
	 */
	public String get(final String name){
		return this.values.get(name);
	}
	
	public String toString(){
		return this.getName() + ", " + this.valueCount() + " values.";
	}

	/**
	 * Compiles all data into a new String, that covers various lines that is suitable for saving to file or printing to screen.
	 */
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
	
	/**
	 * Disposes all data, after this this language pack is unusable.
	 */
	public void dispose(){
		this.values.clear();
		this.values = null;
	}
}
