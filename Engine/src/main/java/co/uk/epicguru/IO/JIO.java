package co.uk.epicguru.IO;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;

public final class JIO {

	private JIO() { }
	
	private static final String TAG = "JIO";
	private static Json json;
	
	private static void createJson(){
		json = new Json();
		json.addClassTag("Vector2", Vector2.class);
		json.addClassTag("Vector3", Vector3.class);
	}
	
	public static void main(String... args){
		

		
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
		
		return json.fromJson(clazz, Json);
	}
}
