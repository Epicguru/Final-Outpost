package co.uk.epicguru.IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import co.uk.epicguru.IO.parsers.JLineParser;
import co.uk.epicguru.IO.port.Base;

/**
 * The Input class in the Final Outpost engine. Takes a file to read from to and reads lines of JCode.
 * @author James Billy
 */
public class JLineReader extends Base{

	private int currentLine = -1;
	private String[] lines;
	private HashMap<String, Object> variables = new HashMap<String, Object>();
	private File file;
	
	/**
	 * Creates a new JLineReader used to read data from a file.
	 * @param file The file to read from. Must not be null.
	 * @throws JLIOException If any of the following: 
	 * <li>1. The file is null.
	 * <li>2. The file does not exist.
	 * <li>3. The file cannot be read from.
	 * <li>4. The reader for the file could not be opened (internal).
	 * <li>5. There was an error reading lines from reader (internal).
	 */
	public JLineReader(File file) throws JLIOException{
		if(file == null){
			throw new JLIOException("File input was null!");
		}
		if(!file.exists()){
			throw new JLIOException("File did not exist. -" + file.getAbsolutePath());
		}
		if(!file.canRead()){
			throw new JLIOException("Application does not have read access to file!. -" + file.getAbsolutePath());
		}
		
		this.file = file;
		
		BufferedReader reader = null;
		
		try{
			reader = new BufferedReader(new FileReader(file));			
		}catch(Exception e){
			error("Could not open reader!", e);
			throw new JLIOException("Could not open reader.", e);
		}
		String line = "";
		ArrayList<String> buffer = new ArrayList<String>();
		try {
			
			// Get all lines
			while((line = reader.readLine()) != null){
				buffer.add(line);
			}
			
			// Copy to array
			lines = new String[buffer.size()];
			lines = buffer.toArray(lines);
			setLines(lines);
			
		} catch (IOException e) {
			throw new JLIOException("Error reading lines after reader creation.", e);
		}
		
		try {
			reader.close();
		} catch (IOException e) {
			// Ignore
		}
		
	}
	
	/**
	 * Gets the file that this reader reads from.
	 */
	public File getFile(){
		return file;
	}
	
	/**
	 * Creates a new JLineReader from multiple lines of JCode.
	 * @param fileContents The contents of a JCode file. (Or any valid JCode)
	 */
	public JLineReader(String fileContents){
		String[] split = fileContents.split("\n");
		setLines(split);
	}
	
	private void setLines(final String[] lines){
		this.lines = lines;
		if(this.lines.length > 0)
			nextLine();
	}

	/**
	 * The other end of JLineWriter.write(key, object).
	 * Reads a variable from the currently loaded variables using a JLineParser.
	 * @param key The key of the variable to load. Same as JLineWriter.write() key.
	 * @return The object the was written, or null if not found. Cast to the correct type.
	 */
	public Object read(final String key){
		return variables.get(key.trim());
	}
	
	/**
	 * Clears all current loaded variables. See {@link #getLoadedValues()}.
	 */
	public void clearBuffer(){
		variables.clear();
	}
	
	/**
	 * Clears the buffer using {@link #clearBuffer()} and then reads all variables from all lines. 
	 * IMPORTANT : If keys on multiple lines are the same then this WILL result in conflicts and incorrect behaviour.
	 */
	public void readAllLines(){
		clearBuffer();
		readRange(0, getLineCount());
	}
	
	/**
	 * Sets the current line of JCode by clearing the buffer and reading all variables from that line.
	 * @param index The line number to read from (Starts a 0 obviously)
	 */
	public void setLine(int index){
		currentLine = index;
		clearBuffer();
		readLine(index);
	}
	
	/**
	 * Clears the buffer and moves on to the next line of JCode.
	 */
	public void nextLine(){
		clearBuffer();
		readLine(++currentLine);
	}
	
	/**
	 * Reads all variables from a given line number. This DOES NOT clear the buffer and DOES NOT set the current line number.
	 * @param index The line to read all varaibles from.
	 */
	public void readLine(int index){
		String line = lines[index];
		
		// 1. Split into variables 
		String[] vars = line.split("-");
		String[] newVars = new String[vars.length - 1];
		int i = 0;
		for(String s : vars){
			if(i != 0)				
				newVars[i - 1] = '-' + s.trim();
			i++;
		}
		
		// 2. Loop		
		for(String variable : newVars){
			
			// 3. Get name : From '-' to '<'. Do not include whitespace.			
			String name = variable.substring(1, variable.indexOf('<')).trim();
			
			// 4. Get prefix. Easy.
			String prefix = variable.substring(variable.indexOf('<') + 1, variable.indexOf('>')).trim();
			
			// 5. Get content
			String content = variable.substring(variable.indexOf('[') + 1, variable.lastIndexOf(']')).trim();
			
			// 6. Load parser
			JLineParser<?> parser = null;
			try {
				parser = JLineParsers.getParser(prefix);
			} catch (JLIOException e) {
				error("Failed to get parser for loaded variable, variable will be ignored. Prefix : '" + prefix + "' Name : '" + name + "' Content : '" + content + "'", e);
				continue;
			}
			
			// 7. Load variable using parser
			Object variableValue = parser.read(name, content, this);
			
			if(variableValue == null){
				error("Parser '" + parser.getClass().getName() + "' returned a null value when reading! Naughty parser! (Variable ignored)");
				continue;
			}
			
			// 8. Put in map
			variables.put(name, variableValue);
			
			// 9. Pat yourself on the back James (pat, pat)
		}
	}
	
	/**
	 * Gets the number of loaded variables.
	 */
	public int getLoadedValuesCount(){
		return getLoadedValues().size();
	}
	
	/**
	 * Gets all loaded and decompiled variables (the buffer) in a HashMap where the Key is the one used
	 * when saving the variable and the Value is the object saved (need to cast).
	 */
	public HashMap<String, Object> getLoadedValues(){
		return variables;
	}
	
	/**
	 * Reads all variables from all lines in the range specified. DOES NOT clear the buffer.
	 * For example to read first 5 lines call readRange(0, 5). This reads lines 0 through 4.
	 * @param start The starting line. Inclusive.
	 * @param end The end line. Exclusive.
	 */
	public void readRange(int start, int end){
		for(int i = start; i < end; i++){
			readLine(i);
		}
	}
	
	/**
	 * Checks if a variable is in the buffer.
	 * @param key The key to look for.
	 * @return True if the key was found in the currently loaded buffer.
	 * @see {@link #getLoadedValues()}
	 */
	public boolean isValueLoaded(final String key){
		for(String key2 : variables.keySet()){
			if(key2.equals(key))
				return true;
		}
		return false;
	}
	
	/**
	 * Gets the array of lines as loaded from the file.
	 * @see {@link #getLinesPretty()}.
	 */
	public String[] getLinesRaw(){
		return lines;
	}
	
	/**
	 * Gets all lines of variables as loaded from file, adding escape characters.
	 * ready to be printed or saved to file.
	 */
	public String getLinesPretty(){
		StringBuilder stringBuilder = new StringBuilder();
		int index = 0;
		for(String s : lines){
			stringBuilder.append(s);
			if(index != lines.length - 1)
				stringBuilder.append('\n');
			index++;
		}
		
		return stringBuilder.toString();
	}
	
	/**
	 * Gets a pretty version of all loaded variables in the format [key : value]
	 * where each variables takes up a line.
	 */
	public String getVariablesPretty(){
		StringBuilder stringBuilder = new StringBuilder();
		int index = 0;
		for(String key : variables.keySet()){
			stringBuilder.append(key);
			stringBuilder.append(" : ");
			stringBuilder.append(variables.get(key).toString());
			if(index != variables.size() - 1)
				stringBuilder.append('\n');
			index++;
		}
		
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the amount of lines of JCode loaded from file or given in constrcutor.
	 */
	public int getLineCount(){
		return getLinesRaw().length;
	}


	/**
	 * Disposes the reader.
	 */
	public void dispose() {
		variables.clear();
		variables = null;
		lines = null;
	}
}
