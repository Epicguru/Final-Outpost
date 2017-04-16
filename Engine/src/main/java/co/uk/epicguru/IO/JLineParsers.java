package co.uk.epicguru.IO;

import java.util.ArrayList;

import co.uk.epicguru.API.U;
import co.uk.epicguru.API.plugins.FinalOutpostPlugin;
import co.uk.epicguru.IO.parsers.JLineParser;
import co.uk.epicguru.logging.Log;
import co.uk.epicguru.main.FOE;

public final class JLineParsers {
	
	public static ArrayList<JLineParser<?>> parsers = new ArrayList<JLineParser<?>>();
	private static ArrayList<JLineParser<?>> temps = new ArrayList<JLineParser<?>>();

	/**
	 * Clears all parsers.
	 */
 	public static void clearParsers(){
		parsers.clear();
		Log.debug("Parsers", "Cleared parsers");
	}
	
 	/**
 	 * Loads all parsers by requesting that all plugin register their parsers.
 	 */
	public static void loadParsers(){
		
		clearParsers();
		
		int old = 0;
		for(FinalOutpostPlugin plugin : FOE.pluginsLoader.getAllPlugins()){
			for(Object o : FOE.pluginsLoader.getExtensions(JLineParser.class, plugin.getWrapper().getPluginId())){
				parsers.add((JLineParser<?>)o);
			}
			Log.info("Parsers", "Plugin '" + plugin.getWrapper().getPluginId() + "' added " + (parsers.size() - old) + " parsers.");
		}
		
		if(parsers.size() == 0){
			Log.error("Parsers", "No parsers loaded! Quitting!");
			return;
		}
		
		Log.info("Parsers", "Loaded Parsers : [" + U.prettify(parsers.toArray(new JLineParser<?>[parsers.size()])) + "]");
	}
	
	/**
	 * Gets the best matched parsers given an object that needs parsing.
	 * @param o The object to find a perser for.
	 * @return The best fit JLineParser. If a perfect match cannot be found, a superclass parser will be used.
	 * @throws JLIOException If no parser could be found for this object or any parser for any superclass.
	 */
	protected static JLineParser<?> getParser(Object o) throws JLIOException{
		temps.clear();
		
		for(JLineParser<?> parser : parsers){
			if(parser.isClass(o)){
				if(parser.isClassExact(o)){
					// Done.
					// Log.error("Parsers", "Found exact : " + parser.getPrefix() + " for object " + o.getClass().getSimpleName());
					return parser;
				}else{
					temps.add(parser);
				}
			}
		}
		
		if(temps.isEmpty()){
			Log.error("", o.getClass().getName());
			throw new JLIOException("No parser found for class " + o.getClass().getName());
		}
		
		JLineParser<?> value = temps.get(0);
		Log.error("Parsers", "Could not find perfect match for class " + o.getClass().getName() + ", using " + value.getType().getName() + " as a substitute.");
		temps.clear();
		return value;
	}
	
	/**
	 * Gets a parser given that parsers' JCode prefix.
	 * @param prefix The parsers JCode prefix.
	 * @return The JLineParser.
	 * @throws JLIOException If the parser could not be found.
	 */
	protected static JLineParser<?> getParser(String prefix) throws JLIOException{
		for(JLineParser<?> parser : parsers){
			if(parser.getPrefix().equals(prefix))
				return parser;
		}
		
		throw new JLIOException("No parser found for prefix '" + prefix + "'");
	}
}
