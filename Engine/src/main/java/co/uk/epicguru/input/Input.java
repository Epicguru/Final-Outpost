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

import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.IO.JLineWriter;
import co.uk.epicguru.logging.Log;
import co.uk.epicguru.main.Constants;
import co.uk.epicguru.main.FOE;
import ro.fortsoft.pf4j.Plugin;

public final class Input {
	
	private static HashMap<String, Integer> keys = new HashMap<>();
	
	static Vector2 tempVec = new Vector2();
	static Vector3 tempVec3 = new Vector3();
	static final String TAG = "Input";
	static boolean[] mouses = new boolean[5 * 2];
	
	/**
	 * Called once every update to refresh values to do with mouse input.
	 */
	public static void update(){
		
		// 0 OLD LEFT
		// 1 LEFT
		// 2 OLD RIGHT
		// 3 RIGHT
		// 4 OLD WHATEVER
		// 5 WHATEVER
		
		for(int i = 1, j = 0; i < mouses.length; i += 2, j++){
			// 1, 3, 5, 7, 9
			mouses[i - 1] = mouses[i];
			mouses[i] = Gdx.input.isButtonPressed(j);
		}
	}
	
	private static int getMouseIndex(int button){
		return button * 2 + 1;
	}
	
	/**
	 * Checks if a mouse button is currently pressed. Uses {@link Buttons}.
	 * @param button The mouse button to check. See {@link Buttons} for a list of buttons (5 available).
	 */
	public static boolean getMouseButton(int button){
		return mouses[getMouseIndex(button)];
	}
	
	/**
	 * Checks if a mouse button WAS pressed last frame. Uses {@link Buttons}.
	 * @param button The mouse button to check. See {@link Buttons} for a list of buttons (5 available).
	 */
	public static boolean getOldMouseButton(int button){
		return mouses[getMouseIndex(button) - 1];
	}
	
	/**
	 * Checks if a mouse button has just been pressed this frame. Uses {@link Buttons} and {@link #getMouseButton(int)} with {@link #getOldMouseButton(int)}. 
	 * @param button The mouse button to check. See {@link Buttons} for a list of buttons (5 available).
	 */
	public static boolean getMouseButtonJustDown(int button){
		return mouses[getMouseIndex(button)] && !mouses[getMouseIndex(button) - 1];
	}
	
	/**
	 * Should only be accessed by plugins, internally.
	 */
	public static void addInput(Plugin plugin, String name, Integer keyNumber){		
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
	
	/**
	 * Should only be accessed by plugins, internally.
	 */
	public static void changeInput(Plugin plugin, String name, Integer newKeyNumber){
		
		if(plugin == null){
			Log.error(TAG, "Plugin not loaded, key change for '" + name + "' ignored");
			return;
		}
		
		String key = plugin.getWrapper().getPluginId() + ":" + name;
		if(keys.containsKey(name)){
			keys.put(key, newKeyNumber);
		}
	}
	
	/**
	 * Should only be accessed by plugins, internally.
	 */
	public static void removeInput(Plugin plugin, String name){
		if(plugin == null){
			Log.error(TAG, "Plugin not loaded, key removal for '" + name + "' ignored");
			return;
		}
		keys.remove(plugin.getWrapper().getPluginId() + ":" + name);
	}
	
	/**
	 * Called once at the beginning of the program before {@link FinalOutpostPlugin.init()}
	 * to load all saved inputs.
	 */
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
	
	/**
	 * Called a few times throughout the program to save the inputs.
	 */
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
	
	/**
	 * Should only be accessed by plugins, internally.
	 */
	public static boolean isInputDown(Plugin plugin, String name){
		if(plugin == null){
			return false;
		}else{
			return isKeyDown(keys.get(plugin.getWrapper().getPluginId() + ":" + name));			
		}
	}
	
	/**
	 * Should only be accessed by plugins, internally.
	 */
	public static boolean isInputJustDown(Plugin plugin, String name){
		if(plugin == null){
			return false;
		}else{
			return isKeyJustDown(keys.get(plugin.getWrapper().getPluginId() + ":" + name));			
		}
	}
	
	/**
	 * Should only be accessed by plugins, internally.
	 */
	public static int getInputCode(Plugin plugin, String name){
		if(plugin == null){
			return -1;
		}else{
			return keys.get(plugin.getWrapper().getPluginId() + ":" + name);		
		}
	}
	
	/**
	 * Should only be accessed by plugins, internally.
	 */
	public static String getInputString(Plugin plugin, String inputName){
		if(plugin == null){
			return null;
		}else{
			return Keys.toString(keys.get(plugin.getWrapper().getPluginId() + ":" + inputName));		
		}
	}
	
	/**
	 * Gets the current width, in PIXELS, of the window.
	 */
	public static int getScreenWidth(){
		return Gdx.graphics.getWidth();
	}
	
	/**
	 * Gets the current height, in PIXELS, of the window.
	 */
	public static int getScreenHeight(){
		return Gdx.graphics.getHeight();
	}
	
	/**
	 * Gets a random int within the bounds, which are BOTH INCLUSIVE, ignoring whether the
	 * start bound is smaller than the end bound.
	 * @param start
	 * @param end
	 * @return
	 */
	public static int random(int start, int end){
		
		if(start < end)
			return MathUtils.random(start, end);
		else
			return MathUtils.random(end, start);
	}
	
	/**
	 * Is a key on the keyboard pressed?
	 * @return True if currently pressed.
	 * @see {@link Keys} for all the keys available.
	 */
	public static boolean isKeyDown(int key){
		return Gdx.input.isKeyPressed(key);
	}
	
	/**
	 * Has a key on the keyboard just been pressed.
	 * Technically, is it pressed this frame but was not pressed last frame?
	 * @return True if currently pressed.
	 * @see {@link Keys} for all the keys available.
	 */
	public static boolean isKeyJustDown(int key){
		return Gdx.input.isKeyJustPressed(key);
	}
	
	/**
	 * Short for {@link Input.getMouseButton(Buttons.LEFT)}
	 */
	public static boolean clickingLeft(){
		return Input.getMouseButton(Buttons.LEFT);
	}
	
	/**
	 * Short for {@link Input.getMouseButtonJustDown(Buttons.LEFT)}
	 */
	public static boolean clickLeft(){
		return Input.getMouseButtonJustDown(Buttons.LEFT);
	}
	
	/**
	 * Short for {@link Input.getMouseButton(Buttons.RIGHT)}
	 */
	public static boolean clickingRight(){
		return Input.getMouseButton(Buttons.RIGHT);
	}
	
	/**
	 * Short for {@link Input.getMouseButtonJustDown(Buttons.RIGHT)}
	 */
	public static boolean clickRight(){
		return Input.getMouseButtonJustDown(Buttons.RIGHT);
	}
	
	/**
	 * Gets the X position, in pixels, of the cursor. This starts
	 * from the bottom right of the window.
	 * Same as {@link Gdx.input.getX()};
	 */
	public static int getMouseX(){
		return Gdx.input.getX();
	}
	
	/**
	 * Gets the Y position, in pixels, of the cursor. This starts
	 * from the bottom right of the window.
	 * This is not the same as {@link Gdx.input.getY()} because that returns with Y starting from the top of the screen.
	 * @see {@link #getMouseYRaw()} for Y starting at the top of the screen, equal to {@link Gdx.input.getY()}.
	 */
	public static int getMouseY(){
		return Gdx.graphics.getHeight() - Gdx.input.getY();
	}
	
	/**
	 * Gets the Y position, in pixels, of the cursor. This starts
	 * from the TOP right of the window.
	 * Same as {@link Gdx.input.getY()};
	 */
	public static int getMouseYRaw(){
		return Gdx.input.getY();
	}
	
	/**
	 * Gets the position of the cursor, in pixels, within the game window.
	 */
	public static Vector2 getMousePos(){
		return tempVec.set(getMouseX(), getMouseY());
	}
	
	/**
	 * Gets the mouse position where the Y axis is top-down.
	 */
	public static Vector2 getMousePosRaw(){
		return tempVec.set(getMouseX(), getMouseYRaw());
	}
	
	/**
	 * Gets the X position of the cursor within the world, using {@link FOE.camera}.
	 * The result will be in TILES, a.k.a. 32px as seen in {@link Constants}.
	 */
	public static float getMouseWorldX(){
		return getMouseWorldX(FOE.camera);		
	}
	
	/**
	 * Gets the Y position of the cursor within the world, using {@link FOE.camera}.
	 * The result will be in TILES, a.k.a. 32px as seen in {@link Constants}.
	 */
	public static float getMouseWorldY(){
		return getMouseWorldY(FOE.camera);		
	}
	
	/**
	 * Gets the position of a cursor using the camera to unproject the screen coordinates.
	 */
	public static float getMouseWorldX(OrthographicCamera cam){
		
		tempVec3.set(getMousePosRaw(), 1);
		cam.unproject(tempVec3);
		return tempVec3.x;
		
	}
	
	/**
	 * Gets the position of a cursor using the camera to unproject the screen coordinates.
	 */
	public static float getMouseWorldY(OrthographicCamera cam){
		
		tempVec3.set(getMousePosRaw(), 1);
		cam.unproject(tempVec3);
		return tempVec3.y;
		
	}
	
	/**
	 * Gets the position of a cursor using the camera to unproject the screen coordinates.
	 */
	public static Vector2 getMouseWorldPos(OrthographicCamera cam){
		tempVec.set(getMouseWorldX(cam), getMouseWorldY(cam));
		return tempVec;
	}
	
	/**
	 * Gets the position of the cursor within the game world, using {@link FOE.camera}.
	 * The vector returned is in TILES. (32 pixels, as see in {@link Constants}).
	 */
	public static Vector2 getMouseWorldPos(){
		tempVec.set(getMouseWorldX(FOE.camera), getMouseWorldY(FOE.camera));
		return tempVec;
	}
	
}
