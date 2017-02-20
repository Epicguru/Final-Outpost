package co.uk.epicguru.IO.parsers;

import co.uk.epicguru.API.Base;
import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.IO.JLineWriter;

public abstract class JLineParser<T> extends Base {

	private String prefix;
	private Class<T> t;
	
	/**
	 * Please pass the class that this JLineParser is intended for. (Exmaple : MyThing.class, Something.class)
	 * @param clazz The class that this parser targets. Should be the same as T.
	 * @param prefix The prefix that should be used in JCode. Keep as short as possible whilst ensuring that it is not a repeat of other parsers.
	 */
	public JLineParser(Class<T> clazz, String prefix){
		this.prefix = prefix;
		this.t = clazz;
	}
	
	/**
	 * Gets this parses prefix.
	 */
	public String getPrefix(){
		return prefix;
	}
	
	/**
	 * Gets the class which this parsers parses.
	 */
	public Class<T> getType(){
		return t;
	}
	
	public T castObject(Object object){
		return t.cast(object);
	}
	
	/**
	 * Checks weather the object matches the class (T) or is a subclass of it.
	 */
	public boolean isClass(Object object){
		return t.isInstance(object);
	}
	
	/**
	 * Weather the object matches the parsers type (T) exactly.
	 */
	public boolean isClassExact(Object object){
		return t.equals(object.getClass());
	}
	
	/**
	 * Gets the name of the class that this is parsing.
	 */
	public String toString(){
		return t.getSimpleName();
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Internal method.
	 */
	public void writeInternal(Object object, String key, JLineWriter writer){
		write((T)object, key, writer);
	}
	
	public abstract void write(T object, String key, JLineWriter writer);
	public abstract T read(String key, String content, JLineReader reader);
	
}
