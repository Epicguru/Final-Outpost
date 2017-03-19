package co.uk.epicguru.input;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import co.uk.epicguru.API.plugins.FinalOutpostPlugin;
import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.IO.JLineWriter;
import co.uk.epicguru.logging.Log;
import co.uk.epicguru.main.FOE;

public final class Input {
	
	private static HashMap<String, Integer> keys = new HashMap<>();
	
	static Vector2 tempVec = new Vector2();
	static Vector3 tempVec3 = new Vector3();
	static boolean oldMousePressed;
	static boolean mousePressed;
	static boolean rightPressed;
	static boolean oldRightPressed;
	static final String TAG = "Input";
	
	public static void update(){
		
		oldMousePressed = mousePressed;
		oldRightPressed = rightPressed;
		mousePressed = Gdx.input.isButtonPressed(Buttons.LEFT);
		rightPressed = Gdx.input.isButtonPressed(Buttons.RIGHT);
		
	}
	
	public static void addInput(FinalOutpostPlugin plugin, String name, Integer keyNumber){
		// TODO document all this class.
		
		if(plugin == null){
			Log.error(TAG, "Plugin not loaded, key '" + name + "' ignored");
			return;
		}
		
		String key = plugin.getWrapper().getPluginId() + ":" + name;
		if(!keys.containsKey(key)){
			keys.put(key, keyNumber);			
		}else{
			// Loaded from file or already present.
		}
	}
	
	public static void changeInput(FinalOutpostPlugin plugin, String name, Integer newKeyNumber){
		
		if(plugin == null){
			Log.error(TAG, "Plugin not loaded, key change for '" + name + "' ignored");
			return;
		}
		
		String key = plugin.getWrapper().getPluginId() + ":" + name;
		if(keys.containsKey(name)){
			keys.put(key, newKeyNumber);
		}
	}
	
	public static void removeInput(FinalOutpostPlugin plugin, String name){
		if(plugin == null){
			Log.error(TAG, "Plugin not loaded, key removal for '" + name + "' ignored");
			return;
		}
		keys.remove(plugin.getWrapper().getPluginId() + ":" + name);
	}
	
	public static void loadInputs(){	
		
		Log.info(TAG, "Loading all input keys...");
		
		File file = new File(FOE.gameDirectory + FOE.inputDirectory + "Keys.txt");
		
		// Ensure that file exists
		if(!file.exists()){
			file.getParentFile().mkdirs();
			try {
				if(!file.createNewFile()){
					Log.error(TAG, "Error loading input keys! (Creating file)");
				}
			} catch (IOException e) {
				Log.error(TAG, "Error loading input keys!", e);
				return;
			}
		}
		
		try {
			JLineReader reader = new JLineReader(file);
			reader.readAllLines();
			
			for(String key : reader.getLoadedValues().keySet()){
				
				String plugin = key.split(":")[0];
				String name = key.split(":")[1];
				String value = (String)reader.read(key);
							
				addInput(FOE.pluginsLoader.getFOPlugin(plugin), name, Keys.valueOf(value));
				Log.info(TAG, plugin + "'s input '" + name + "' is mapped to " + value + "(" + Keys.valueOf(value) + ")");
			}
			
		} catch (Exception e){
			Log.error(TAG, "Error loading input keys!", e);
			return;
		}	
	}
	
	public static void saveInputs(){
		
		Log.info(TAG, "Saving all input keys...");
		
		File file = new File(FOE.gameDirectory + FOE.inputDirectory + "Keys.txt");
		try {
			JLineWriter writer = new JLineWriter(file);
			
			for(String key : keys.keySet()){
				Log.info(TAG, key);
				writer.writeLine(key, Keys.toString(keys.get(key)));
			}
			
			writer.save();
			
		} catch (Exception e){
			Log.error(TAG, "Error saving inputs!", e);
		}
	}
	
	public static boolean isInputDown(FinalOutpostPlugin plugin, String name){
		if(plugin == null){
			return false;
		}else{
			return isKeyDown(keys.get(plugin.getWrapper().getPluginId() + ":" + name));			
		}
	}
	
	public static boolean isInputJustDown(FinalOutpostPlugin plugin, String name){
		if(plugin == null){
			return false;
		}else{
			return isKeyJustDown(keys.get(plugin.getWrapper().getPluginId() + ":" + name));			
		}
	}
	
	public static int getInputCode(FinalOutpostPlugin plugin, String name){
		if(plugin == null){
			return -1;
		}else{
			return keys.get(plugin.getWrapper().getPluginId() + ":" + name);		
		}
	}
	
	public static String getInputString(FinalOutpostPlugin plugin, String inputName){
		if(plugin == null){
			return null;
		}else{
			return Keys.toString(keys.get(plugin.getWrapper().getPluginId() + ":" + inputName));		
		}
	}
	
	public static int getScreenWidth(){
		return Gdx.graphics.getWidth();
	}
	
	public static int getScreenHeight(){
		return Gdx.graphics.getHeight();
	}
	
	public static int random(int start, int end){
		
		if(start < end)
			return MathUtils.random(start, end);
		else
			return MathUtils.random(end, start);
	}
	
	public static boolean isKeyDown(int key){
		return Gdx.input.isKeyPressed(key);
	}
	
	public static boolean isKeyJustDown(int key){
		return Gdx.input.isKeyJustPressed(key);
	}
	
	public static boolean clickingLeft(){
		return mousePressed;
	}
	
	public static boolean clickLeft(){
		return mousePressed && !oldMousePressed;
	}
	
	public static boolean clickingRight(){
		return rightPressed;
	}
	
	public static boolean clickRight(){
		return rightPressed && !oldRightPressed;
	}
	
	public static int getMouseX(){
		return Gdx.input.getX();
	}
	
	public static int getMouseY(){
		return Gdx.input.getY();
	}
	
	public static Vector2 getMousePos(){
		return tempVec.set(getMouseX(), getMouseY());
	}
	
	public static float getMouseWorldX(){
		return getMouseWorldX(FOE.camera);		
	}
	
	public static float getMouseWorldY(){
		return getMouseWorldY(FOE.camera);		
	}
	
	public static float getMouseWorldX(OrthographicCamera cam){
		
		tempVec3.set(getMousePos(), 1);
		cam.unproject(tempVec3);
		return tempVec3.x;
		
	}
	
	public static float getMouseWorldY(OrthographicCamera cam){
		
		tempVec3.set(getMousePos(), 1);
		cam.unproject(tempVec3);
		return tempVec3.y;
		
	}
	
	public static Vector2 getMouseWorldPos(OrthographicCamera cam){
		tempVec.set(getMouseWorldX(cam), getMouseWorldY(cam));
		return tempVec;
	}
	
	public static Vector2 getMouseWorldPos(){
		tempVec.set(getMouseWorldX(FOE.camera), getMouseWorldY(FOE.camera));
		return tempVec;
	}
	
}
