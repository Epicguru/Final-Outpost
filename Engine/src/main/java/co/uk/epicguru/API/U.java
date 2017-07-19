package co.uk.epicguru.API;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.reflections.Reflections;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;

import co.uk.epicguru.logging.Log;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.progress.ProgressMonitor;

public final class U {
	private U() {}	
	private static final String TAG = "Utilities";
	private static Color colour = new Color();
	private static Interpolation interpolation = Interpolation.linear;
	private static HashMap<String, Long> timers = new HashMap<>();
	
	/**
	 * Replaces chunks of a string with blank space.
	 * @param original The original string to replace in.
	 * @param toRemove The chunk of string to remove from the original string.
	 * @return The new String. This DOES create a new string object.
	 */
	public static String removeString(String original, String toRemove){
		return new String(original).replace(toRemove, "");
	}
	
	public static String prettify(Object[] objects){
		StringBuilder stringBuilder = new StringBuilder();
		
		int index = 0;
		for(Object o : objects){
			
			if(index != 0 && index != objects.length){
				stringBuilder.append(", ");
			}
			stringBuilder.append(o.toString());
			
			index++;
		}
		
		return stringBuilder.toString();
	}
	
	/**
	 * Gets all files with the specified endings in the directory 'root'.
	 * @param root The root directory to search. This IS recursive.
	 * @param endings The file endings to search for.
	 * @return The array of files found.
	 */
	public static File[] getFilesWithEnding(File root, String... endings){
		
		if(root == null){
			Log.error(TAG, "Root to scan was null!");
			return null;
		}
		if(!root.exists()){
			Log.error(TAG, "Root to scan does not exist -" + root.getAbsolutePath());
			return null;
		}
		if(!root.isDirectory()){
			Log.error(TAG, "Root to scan was not a directory! -" + root.getAbsolutePath());
			return null;
		}
		
		String[] newEndings = new String[endings.length];
		int index = 0;
		for(String s : endings){
			newEndings[index] = removeString(s, ".");
			index++;
		}
		
		Collection<File> files = FileUtils.listFiles(root, newEndings, true);
		Log.debug(TAG, "Searching for [" + prettify(newEndings) + "] in " + root.getPath());
		for(File file : files){
			Log.debug(TAG, "--" + file.getName());
		}
		File[] newFiles = new File[files.size()];
		return files.toArray(newFiles);
	}
	
	public static File[] getAssetFolders(File root){
		if(root == null){
			Log.error(TAG, "Root to scan was null!");
			return null;
		}
		if(!root.exists()){
			Log.error(TAG, "Root to scan does not exist -" + root.getAbsolutePath());
			return null;
		}
		if(!root.isDirectory()){
			Log.error(TAG, "Root to scan was not a directory! -" + root.getAbsolutePath());
			return null;
		}
		
		ArrayList<File> files = new ArrayList<>();
		
		for(File file : root.listFiles()){
			if(!file.isDirectory())
				continue;
			String[] names = file.list();
			if(names == null)
					continue;
			for(String name : names){
				if(name.equals("assets")){
					files.add(new File(file.getAbsolutePath() + '\\' + name));
					continue;
				}
			}
		}
		
		return files.toArray(new File[files.size()]);
	}
	
	/**
	 * Gets all classes under packageRoot that are subclasses of baseClass.
	 * @param packageRoot The package to load from. Includes sub packages. (Example : "com.you.tests.things")
	 * @param baseClass The base class to look for sub-classes of. (Example : MyObject.class or myObject.getClass())
	 * @return The array of classes found.
	 */
	public static Class<?>[] getClasses(String packageRoot, Class<?> baseClass){
		Reflections reflections = new Reflections(packageRoot);
		Set<?> classes = reflections.getSubTypesOf(baseClass);
		Class<?>[] classes2 = new Class<?>[classes.size()];
		int index = 0;
		for(Object clazz : classes){
			Class<?> clazz2 = (Class<?>)clazz;
			classes2[index++] = clazz2;
		}
		
		return classes2;
	}
	
	/**
	 * Gets instances of all classes that sub-class baseClass within packageRoot.
	 * The classes found must have a no-parameters constructor.
	 * @param packageRoot The package to look in. Includes sub-packages.
	 * @param baseClass The base class to look for sub-classes of.
	 * @return An array of objects loaded.
	 */
	public static Object[] getClassInstances(String packageRoot, Class<?> baseClass){
		Class<?>[] classes = getClasses(packageRoot, baseClass);
		Object[] objects = new Object[classes.length];
		
		int index = 0;
		for(Class<?> clazz : classes){
			try {
				objects[index++] = clazz.getConstructor().newInstance();
			} catch (NoSuchMethodException e) {
				Log.error(TAG, "Could not find no param contrcutor for " + clazz.getName(), e);
			} catch (SecurityException e) {
				Log.error(TAG, "Could not find no param contrcutor for " + clazz.getName(), e);				
			} catch (InstantiationException e) {
				Log.error(TAG, "Constructor call failed for" + clazz.getName(), e);
			} catch (IllegalAccessException e) {
				Log.error(TAG, "Constructor call failed for" + clazz.getName(), e);
			} catch (IllegalArgumentException e) {
				Log.error(TAG, "Constructor call failed for" + clazz.getName(), e);
			} catch (InvocationTargetException e) {
				Log.error(TAG, "Constructor call failed for" + clazz.getName(), e);
			}
		}
		
		return objects;
	}

	/**
	 * Gets the name of a file or asset from a final path.
	 */
	public static String nameFromPath(String path, char c){
		if(path == null)
			return null;
		int index = path.lastIndexOf(c);
		return path.substring(index == -1 ? 0 : index + 1, path.length());
	}
	
	/**
	 * Gets the name of a file or asset from a final path.
	 */
	public static String nameFromPath(String path){
		return nameFromPath(path, '/');
	}
	
	/**
	 * Gets the name of a file or asset from a final path, excluding the file extension.
	 */
	public static String nameFromPathNoExtension(String path, char c){
		String s = nameFromPath(path, c);
		return s.substring(0, s.lastIndexOf('.'));
	}
	
	/**
	 * Gets the name of a file or asset from a final path, excluding the file extension.
	 */
	public static String nameFromPathNoExtension(String path){
		return nameFromPathNoExtension(path, '/');
	}
	
	/**
	 * Gets the current operation of a ZipFile.
	 */
	public static String getCurrentOperation(ZipFile zip){
		
		if(zip == null)
			return "Unknown";
		
		switch(zip.getProgressMonitor().getCurrentOperation()){
			case ProgressMonitor.OPERATION_EXTRACT:
				return "Extracting";
			case ProgressMonitor.OPERATION_ADD:
				return "Adding";
			case ProgressMonitor.OPERATION_MERGE:
				return "Merging";
			case ProgressMonitor.OPERATION_REMOVE:
				return "Removing";
			case ProgressMonitor.OPERATION_CALC_CRC:
				return "CALC_CRC"; // What the hell is this xD
			case ProgressMonitor.OPERATION_NONE:
				return "None";
		}
		
		return "Unknown";
	}
	
	/**
	 * Starts a new timer called timerName.
	 */
	public static void startTimer(final String timerName){
		timers.put(timerName, System.nanoTime());
	}
	
	/**
	 * Gets the time elapsed since the timer was started. (In SECONDS)
	 * @param timerName The name of the timer.
	 * @return The time since the timer was started.
	 */
	public static float endTimer(final String timerName){
		Long l = timers.remove(timerName);
		if(l == null){
			Log.error(TAG, "Timer called " +  timerName + " does not exist!");
			return -1;
		}
		return (System.nanoTime() - l) / (1000f * 1000f * 1000f);
	}
	
	/**
	 * Gets the time elapsed since the timer was started. (In MILLISECONDS. 1000 millis = 1 second)
	 * @param timerName The name of the timer.
	 * @return The time in seconds since the timer was started.
	 */
	public static long endTimerMillis(final String timerName){
		Long l = timers.remove(timerName);
		if(l == null){
			Log.error(TAG, "Timer called " +  timerName + " does not exist!");
			return -1;
		}
		return (System.nanoTime() - l) / (1000 * 1000);
	}
	
	/**
	 * Gets the time elapsed since the timer was started. (In NANOSECONDS. 1,000,000,000 nanos = 1 second)
	 * <li>
	 * Fun fact: Your processor (at 4Gz) has a cycle time of 0.25 nanoseconds, and the average life of a molecule of positronium hydride is 0.5 nanoseconds.
	 * <li>
	 * Also, in one cycle of said CPU light will travel around 7.5cm.
	 * <li>
	 * Now you know. You're welcome.
	 * @param timerName The name of the timer.
	 * @return The time in seconds since the timer was started.
	 */
	public static long endTimerNanos(final String timerName){
		Long l = timers.remove(timerName);
		if(l == null){
			Log.error(TAG, "Timer called " +  timerName + " does not exist!");
			return -1;
		}
		return (System.nanoTime() - l);
	}

	/**
	 * Gets the type of interpolation used in lerp methods.
	 * Defaults to linear interpolation.
	 * @see {@link #setInterpolation(Interpolation)}
	 */
	public static Interpolation getInterpolation(){
		return interpolation;
	}
	
	/**
	 * Sets the type of interpolation used in lerp methods.
	 */
	public static void setInterpolation(Interpolation interpolation){
		if(interpolation != null)
			U.interpolation = interpolation;
	}
	
	/**
	 * Lerps between two colours using the {@link #getInterpolation() current interpolation}.
	 * @param a The first colour.
	 * @param b The second colour.
	 * @param p The percentage between the first and the second colour.
	 * @return The interpolated colour. Same instance returned every time.
	 */
	public static Color lerp(Color a, Color b, float p){
		
		colour.r = interpolation.apply(a.r, b.r, p);
		colour.g = interpolation.apply(a.g, b.g, p);
		colour.b = interpolation.apply(a.b, b.b, p);
		colour.a = interpolation.apply(a.a, b.a, p);
		
		return colour;
		
	}
}
