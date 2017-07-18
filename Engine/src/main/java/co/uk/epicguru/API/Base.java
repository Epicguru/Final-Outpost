package co.uk.epicguru.API;

import co.uk.epicguru.logging.Log;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.ExtensionPoint;

/**
 * The utility class that also acts as an ExtensionPoint. Includes functions to debug and log.
 * @author Billy
 */
@Extension
public class Base implements ExtensionPoint {
	
	private String _TAG = getClass().getSimpleName();
	
	/**
	 * Gets the tag that is used in logging functions.
	 * By default is equal to the name of the class.
	 * @see {@link #setTag(String)}
	 */
	public String getTag(){
		return _TAG;
	}
	
	/**
	 * Sets the logging tag. By default is equal to the name of the class.
	 * @param text The new tag to debug with.
	 */
	public void setTag(String TAG){
		this._TAG = TAG;
	}
	
	/**
	 * Equivalent to {@link #info(String)}.
	 * @param text The text to print.
	 */
	public void print(String text){
		info(text);
	}
	
	/**
	 * Logs some text to the console.
	 * Equivalent to Log.info(getTag(), text)
	 * @param text The text to print.
	 */
	public void info(String text){
		Log.info(getTag(), text);
	}
	
	/**
	 * Logs some text to the console.
	 * Equivalent to Log.debug(getTag(), text)
	 * @param text The text to print.
	 */
	public void debug(String text){
		Log.debug(getTag(), text);
	}
	
	/**
	 * Logs some text to the console.
	 * Equivalent to Log.debug(getTag(), text)
	 * @param text The text to print.
	 */
	public void error(String text){
		Log.error(getTag(), text);
	}
	
	/**
	 * Logs some text to the console.
	 * Equivalent to Log.debug(getTag(), text)
	 * @param text The text to print.
	 * @param e The Exception subclass to log.
	 */
	public void error(String text, Exception e){
		Log.error(getTag(), text, e);
	}
	
	/**
	 * Logs some text to the console.
	 * Equivalent to Log.error(getTag(), text, exception)
	 * @param text The text to print.
	 * @param exception The exception to log info about.
	 */
	public void debug(String text, Exception exception){
		Log.error(getTag(), text, exception);
	}
	
}
