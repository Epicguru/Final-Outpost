package co.uk.epicguru.IO;

import java.util.ArrayList;

import co.uk.epicguru.IO.parsers.JLineParser;
import co.uk.epicguru.IO.parsers.instances.BooleanArrayParser;
import co.uk.epicguru.IO.parsers.instances.BooleanParser;
import co.uk.epicguru.IO.parsers.instances.ByteArrayParser;
import co.uk.epicguru.IO.parsers.instances.ByteParser;
import co.uk.epicguru.IO.parsers.instances.FloatArrayParser;
import co.uk.epicguru.IO.parsers.instances.FloatParser;
import co.uk.epicguru.IO.parsers.instances.IntegerArrayParser;
import co.uk.epicguru.IO.parsers.instances.IntegerParser;
import co.uk.epicguru.IO.parsers.instances.LongArrayParser;
import co.uk.epicguru.IO.parsers.instances.LongParser;
import co.uk.epicguru.IO.parsers.instances.StringArrayParser;
import co.uk.epicguru.IO.parsers.instances.StringParser;

public final class JLineParsers {
	
	public static ArrayList<JLineParser<?>> parsers = new ArrayList<JLineParser<?>>();
	private static ArrayList<JLineParser<?>> temps = new ArrayList<JLineParser<?>>();

	/**
	 * Clears all parsers.
	 */
 	public static void clearParsers(){
		parsers.clear();
	}
	
 	/**
 	 * Loads all parsers by requesting that all plugin register their parsers.
 	 */
	public static void loadParsers(){
		
		clearParsers();
		parsers.add(new BooleanArrayParser());
		parsers.add(new BooleanParser());
		parsers.add(new ByteArrayParser());
		parsers.add(new ByteParser());
		parsers.add(new FloatArrayParser());
		parsers.add(new FloatParser());
		parsers.add(new IntegerArrayParser());
		parsers.add(new IntegerParser());
		parsers.add(new LongArrayParser());
		parsers.add(new LongParser());
		parsers.add(new StringArrayParser());
		parsers.add(new StringParser());
		
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
			throw new JLIOException("No parser found for class " + o.getClass().getName());
		}
		
		JLineParser<?> value = temps.get(0);
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
