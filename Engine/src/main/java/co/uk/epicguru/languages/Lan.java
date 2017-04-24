package co.uk.epicguru.languages;

import java.util.HashMap;
import java.util.Set;

import co.uk.epicguru.languages.utils.LanguagePack;

public final class Lan {

	private static HashMap<String, LanguagePack> packs = new HashMap<String, LanguagePack>();
	private static LanguagePack current;
	
	/*
	 * We need to have a system that both allows for official languages, such as
	 * English or Spanish. However we also want to enable other users to have custom languages and
	 * random LanguagePacks. Lets use a HashMap and a simple 'current language' system.
	 */

	/**
	 * Gets the currently active {@link LanguagePack}. This should never be null.
	 * @see {@link #setCurrentLanguage(String)} to set this.
	 */
	public static LanguagePack current(){
		return current;
	}
	
	/**
	 * Sets the currently active language. The language must be loaded using {@link #add(LanguagePack)},
	 * and if it is not found no changes will be made to the current language. See {@link #remove(LanguagePack)}
	 * to remove old languages.
	 * @param name The name of the language pack.
	 */
	public static void setCurrentLanguage(String name){
		current = get(name) == null ? current() : get(name);		
	}
	
	/**
	 * Gets the current amount of languages loaded.
	 */
	public static int getLangCount(){
		return packs.size();
	}
	
	/**
	 * Gets a list of all loaded languages names. Please do not modify.
	 */
	public static Set<String> getLangNames(){
		return packs.keySet();
	}
	
	/**
	 * Clears all languages.
	 */
	public static void clear(){
		setCurrentLanguage(null);
		packs.clear();
	}
	
	/**
	 * Gets a string value from the current active language. This is how you get text from languages.
	 * If the current language does not have the string you are looking for, the string 'LANGUAGE:NAME' will
	 * be returned where LANGUAGE is replaced with the name of the current language and NAME will be replaced with
	 * the name parameter. Passing null as name will always fail.
	 * @param name The name of the text you are searching for. For example,
	 * <li> 'TILE DIRT' would return 'Dirt' or 'Tierra' or 'Sol'
	 * <li> 'PLAY GAME' would return 'Play' or 'Jugar' or 'Jouer'
	 * @return The found text in the current language. See above for info on what happens if not found.
	 * @see {@link #current()} to get current language.
	 */
	public static String str(final String name){
		if(current == null){
			return "NO LANG!";
		}else{
			String value = current.get(name);
			if(value == null){
				return current.getName() + ":" + name;
			}else{
				return value;
			}
		}
	}
	
	/**
	 * Adds a language pack to the loaded list. See {@link #remove(LanguagePack)} to remove.
	 * @param language The language. If null, is ignored.
	 */
	public static void add(LanguagePack language){
		if(language == null)
			return;
		packs.put(language.getName(), language);
	}
	
	/**
	 * Removes a language that has been added using {@link #add(LanguagePack)}.
	 * @param lang The language, if null ignored.
	 */
	public static void remove(LanguagePack lang){
		if(lang == null)
			return;
		if(lang != null)
			remove(lang.getName());
	}
	
	/**
	 * Removes a language given a name. The language pack must have been previously added using {@link #add(LanguagePack)}.
	 * @param name The name of the language to remove, if null this call is ignored.
	 */
	public static void remove(String name){
		if(name == null)
			return;
		packs.remove(name);
	}
	
	/**
	 * Gets a loaded language pack given its name. If null is passed, null will be returned.
	 * @param name The name of the language pack to get.
	 * @return The language pack if loaded, null if not in the loaded list.
	 * @see {@link #add(LanguagePack)} to add languages to the loaded list.
	 */
	public static LanguagePack get(final String name){
		return packs.get(name);
	}
}
