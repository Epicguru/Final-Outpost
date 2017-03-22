package co.uk.epicguru.IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.utils.Disposable;

import co.uk.epicguru.API.Base;
import co.uk.epicguru.IO.parsers.JLineParser;
import co.uk.epicguru.logging.Log;

/**
 * The Input class in the Final Outpost engine. Takes a file to read from to and reads lines of JCode.
 * @author James Billy
 */
public class JLineReader extends Base implements Disposable{

	private int currentLine = -1;
	private String[] lines;
	private HashMap<String, Object> variables = new HashMap<String, Object>();
	private ArrayList<String> tempStrings = new ArrayList<>();
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
				
		// 0. Whooo!
		tempStrings.clear();
		
		// 1. Check if line is empty or comment
		if(line == null)
			return;
		if(line.trim().isEmpty())
			return;
		if(line.trim().charAt(0) == '#')
			return;
		
		char split = '-';
		int found = 0;
		StringBuilder str = new StringBuilder();
		
		// 2. Get variables and trim
		char[] letters = line.trim().toCharArray();
		for(int i = 0; i < letters.length; i++){
			char current = letters[i];
			
			if(current == split){
				tempStrings.add(str.toString().trim());
				str.setLength(0);
				found++;
			}else{
				str.append(current);
			}
		}
		
		str.setLength(0);
		
		// 3. Check length
		if(found == 0)
			return; // Not worth reading
		
		// 4. Read each var
		for(String string : tempStrings){
			char[] l = string.toCharArray();
			boolean pastType = false;
			boolean finishedType = false;
			boolean pastContent = false;
			boolean finishedContent = false;
			char typeA = '<';
			char typeB = '>';
			char contentA = '[';
			char contentB = ']';
			
			String name = null;
			String type = null;
			String content = null;
			
			int currentIndex = 0;
			for(char c : l){
				if(!pastType){
					// In name
					if(c == typeA){
						pastType = true;
						name = str.toString();
						
						// Check name for bad chars
						if(JLineIO.containsESCs(name)){
							RuntimeException e = new RuntimeException("The name of a JLineVariable cannot contain any of the following : \n" + 
									JLineIO.getEscapableCharacters().toString()
									);
							Log.error("JLine IO", "JLineIO name invalid : '" + name + "'", e);
							throw e;
						}
						
						str.setLength(0);
					}else{
						str.append(c);
					}
				}else{
					if(!finishedType){
						// Looking for >
						if(c == typeB){
							// Found end of type
							type = str.toString().trim();
							finishedType = true;
						}else{
							str.append(c);
						}
					}else{
						if(!pastContent){
							// Looking for [
							if(c == contentA){
								// Found start of content
								// Nothing to save
								str.setLength(0);
								pastContent = true;
							}
						}else{
							if(!finishedContent){
								// Be careful with escape chars...
								if(c == contentB){
									if(letters[currentIndex - 1] != getEscapeChar()){
										finishedContent = true;
										content = str.toString().trim();
										str.setLength(0);
									}else{
										str.append(c);
									}
								}else{
									str.append(c);
								}
							}
						}
					}
				}
				currentIndex++;
				
				if(finishedContent)
					break;
			}
			
			// 5. Errors, if any
			if(!pastType){
				RuntimeException e = new RuntimeException("Error whilst pasring JLineIO : \n"
						+ "Did not find beggining of TYPE (<) in line!");
				Log.error("JLine IO", "Error in line parsing.", e);
				throw e;
			}
			if(!finishedType){
				RuntimeException e = new RuntimeException("Error whilst pasring JLineIO : \n"
						+ "Did not find end of TYPE (>) in line!");
				Log.error("JLine IO", "Error in line parsing.", e);
				throw e;
			}
			if(!pastContent){
				RuntimeException e = new RuntimeException("Error whilst pasring JLineIO : \n"
						+ "Did not find beggining of CONTENT ([) in line!");
				Log.error("JLine IO", "Error in line parsing.", e);
				throw e;
			}
			if(!finishedContent){
				RuntimeException e = new RuntimeException("Error whilst pasring JLineIO : \n"
						+ "Did not find end of CONTENT (]) in line!");
				Log.error("JLine IO", "Error in line parsing.", e);
				throw e;
			}
			
			// 6. Read, read!
			JLineParser<?> parser = null;
			try {
				parser = JLineParsers.getParser(type);
			} catch (JLIOException e) {
				RuntimeException e2 = new RuntimeException("Error whilst pasring JLineIO : \n"
						+ "Could not find parser for type '" + type + "'", e);
				Log.error("JLine IO", "Error in line parsing.", e2);
				throw e2;
			}
			
			Object o = parser.read(name, content, this);
			variables.put(name, o);
			
			// Done!
		}
	}
	
	/**
	 * Gets the escape character used in JLineIO. Just in case you were curious...
	 */
	public final char getEscapeChar(){
		return JLineIO.escape;
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
