package co.uk.epicguru.IO;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import co.uk.epicguru.logging.Log;
import co.uk.epicguru.main.FOE;

public final class JIO {

	private JIO() { }
	
	private static final String TAG = "JIO";
	private static MyJson json;
	
	private static void createJson(){
		json = new MyJson();
		json.setIgnoreDeprecated(true);
		
		// Defaults, for the pretties :D (and space).
		json.addClassTag("Bool", Boolean.class);
		json.addClassTag("String", String.class);
		json.addClassTag("Vector2", Vector2.class);
		json.addClassTag("Vector3", Vector3.class);
		json.addClassTag("Integer", Integer.class);
		json.addClassTag("Float", Float.class);
		json.addClassTag("Double", Double.class);
		json.addClassTag("Byte", Byte.class);
		
		// Get from plugins.
		FOE.pluginsLoader.getAllClassTags((clazz, tag) -> {
			json.addClassTag(tag, clazz);
		});
	}
	
	/**
	 * Creates a Json string from an object.
	 * @param o The object. If this object is null, null will be returned.
	 * @param pretty If true then the output will look all pretty-like :)
	 * @return The single or multi-line json output, or null if the object was null.
	 */
	public static String toJson(Object o, boolean pretty){
		if(o == null)
			return null;
		
		if(json == null) createJson();
		
		String value = json.toJson(o, o.getClass());
		
		if(!pretty) 
			return value;
		else 
			return json.prettyPrint(value);
	}
	
	public static <T> T fromJson(String Json, Class<T> clazz){
		if(Json == null || Json.isEmpty())
			return null;
		if(json == null) createJson();
		try{			
			return json.fromJson(clazz, Json);
		}catch(Exception e){
			Log.error(TAG, "Error parsing json into object type '" + clazz.getName() + "'\n"
					+ "File - UKN" + "\n"
					+ "Contents - " + Json + "\n"
					+ "Game will exit.", e);
			Log.saveLog();
			System.exit(-1); // Crashing the hard way! Yea!
			return null;
		}
	}
}