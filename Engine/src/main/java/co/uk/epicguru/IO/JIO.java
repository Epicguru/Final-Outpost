package co.uk.epicguru.IO;

import java.io.IOException;
import java.io.StringWriter;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import co.uk.epicguru.logging.Log;

public final class JIO {

	private JIO() { }
	
	private static final String TAG = "JIO";
	private static Json json;
	private static boolean writing;
	
	private static void createJson(){
		json = new Json();
	}
	
	public static void main(String... args){
		
		Json j = startWrite();
		
		j.writeValue("Test Thing", new Vector3(1, 2, 3));	
		j.writeValue("Int Thing", 123);
		
		Log.info(TAG, endWrite(true));
		
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
		if(writing){
			Log.error(TAG, "Cannot convert to json when the json is writing!");
			return null;
		}
		
		if(json == null) createJson();
		
		String value = json.toJson(o, o.getClass());
		
		if(!pretty) 
			return value;
		else 
			return json.prettyPrint(value);
	}
	
	public static Json writeValue(String key, Object value){
		return writeValue(key, value, null, null);
	}
	
	public static Json writeValue(String key, Object value, Class<?> base, Class<?> real){
		if(json == null)
			createJson();
		if(!writing)
			return json;
		if(key == null){
			Log.error(TAG, "Key (field name) cannot be null!");
			return json;
		}
		
		json.writeValue(key, value, base, real);
		
		return json;
	}
	
	public static Json startWrite(){
		if(json == null)
			createJson();
		if(writing){
			Log.error(TAG, "Json is already in write mode!");
			return null;
		}

		json.setWriter(new JsonWriter(new StringWriter()));
		json.writeObjectStart();
		
		writing = true;
		return json;
	}
	
	public static String endWrite(boolean pretty){
		if(json == null){
			return null;
		}
		if(!writing){
			Log.error(TAG, "Json not in write mode!");
			return null;
		}
		
		json.writeObjectEnd();
		
		String value = json.getWriter().getWriter().toString();
		
		try {
			json.getWriter().flush();
		} catch (IOException e) {
			Log.error(TAG, "Could not flush writer!", e);
		}
		json.setWriter(null);
		
		if(!pretty)
			return value;
		else
			return json.prettyPrint(value);
	}
}
