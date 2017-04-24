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
	
	public static LanguagePack current(){
		return current;
	}
	
	public static void setCurrentLanguage(String name){
		current = get(name);		
	}
	
	public static int getLangCount(){
		return packs.size();
	}
	
	public static Set<String> getLangNames(){
		return packs.keySet();
	}
	
	public static void clear(){
		setCurrentLanguage(null);
		packs.clear();
	}
	
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
	
	public static void add(LanguagePack language){
		packs.put(language.getName(), language);
	}
	
	public static LanguagePack get(final String name){
		return packs.get(name);
	}
}
