package co.uk.epicguru.IO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import co.uk.epicguru.API.Base;
import co.uk.epicguru.IO.parsers.JLineParser;

/**
 * The Output class in the Final Outpost engine. Takes a file to write to and writes lines of JCode.
 * @author James Billy
 */
public class JLineWriter extends Base{
	
	private File file;
	private StringBuilder stringBuilder = new StringBuilder();
	private int lineNum = 0;
	private int writes;
	private ArrayList<String> lines = new ArrayList<String>();
	
	/**
	 * Creates a new JLineWriter, that works in conjunction with JLineReader to input and output data.
	 * @param file The file to write to. If null, {@link #save()} is disabled, use {@link #getAllLines()} instead.
	 * @throws JLIOException If the file was NOT null AND the file was not vaild.
	 * @throws IOException If the file did NOT exist and it was failed to be created.
	 */
	public JLineWriter(File file) throws JLIOException, IOException{
		this.file = file;
		
		if(file == null){
			lineNum = -1;
			newLine();
			return;
		}
		
		if(file.isDirectory()){
			throw new JLIOException("File is a directory. -" + file.getAbsolutePath());
		}
		
		if(!file.exists()){
			boolean worked = createFile();
			if(!worked){
				throw new JLIOException("File did not exist (" + !file.exists() + ") and could not be created. -" + file.getAbsolutePath());
			}
		}
		
		lineNum = -1;
		newLine();
	}
	
	/**
	 * Internal method used by JLineParser's. Writes a new variable of JCode.
	 * @param content The content (text between '[' and ']').
	 * @param parser The parser used to encode this content. (Just pass 'this')
	 * @param key The key (name of variable) to write. Please do not change unless there is a very good reason to do so.
	 */
	public void writeRaw(final String content, JLineParser<?> parser, String key){
		String newString = JLineIO.translateIn(content.trim());
		appendToLine(getStart(parser, key) + newString + getEnd(parser, key));
		writes++;
	}
	
	private String getStart(JLineParser<?> parser, String key){
		return '-' + key + '<' + parser.getPrefix() + ">[";
	}
	
	private String getEnd(JLineParser<?> parser, String key){
		return "]";
	}
	
	/**
	 * Gets all current lines of JCode. Includes escape characters.
	 * @return The single-string version of all lines of JCode. This is what is saved to the file in {@link #save()}.
	 */
	public String getAllLines(){
		
		stringBuilder.setLength(0);
		
		int index = 0;
		for(String line : lines){
			stringBuilder.append(line);
			if(index != lines.size() - 1)
				stringBuilder.append('\n');
			index++;
		}
		
		return stringBuilder.toString();
	}
	
	/**
	 * Gets the escape character used in JLineIO. Just in case you were curious...
	 */
	public final char getEscapeChar(){
		return JLineIO.escape;
	}
	
	/**
	 * Saves all lines of JCode to the file. Uses {@link #getAllLines()}.
	 * @return True if operation was successful.
	 */
	public boolean save(){
		
		if(file == null){
			error("The save() is disabled because the file passed with the constructor was null!");
			return false;
		}
		
		print("Saving to " + file.getName() + ", " + writes + " variables in " + lines.size() + " lines of JCode.");
		
		if(!file.canWrite()){
			error("Cannot write to file!");
			return false;
		}
		
		BufferedWriter writer = null;
				
		try {
			writer = new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {
			error("Failed to create internal writer!", e);
			return false;
		}
		
		try {
			// WRITE
			writer.write(getAllLines());
		} catch (IOException e) {
			error("Failed to write liesnto file using writer!", e);
			try {
				writer.close();
			} catch (IOException e1) {
				// Ignore
			}
			return false;
		}
		
		try {
			writer.flush();
			writer.close();
		} catch (Exception e) {
			// Ignore
		}
		
		// GC
		System.gc();
		
		return true;
	}
	
	/**
	 * Starts a new line of JCode after the current line.
	 * @see {@link #getLineNumber()} , {@link #setLineNumber(int)}.
	 */
	public void newLine(){
		lines.add(++lineNum, "");
	}	
	
	private void appendToLine(String appended){
		lines.set(getLineNumber(), lines.get(getLineNumber()) + appended);
	}
	
	private boolean createFile(){
		if(this.file == null){
			error("File is null! File could not be deleted!", new JLIOException("File could not be created, null!"));
			return false;
		}
		boolean a = new File(this.file.getParent()).mkdirs();
		if(new File(this.file.getParent()).exists())
			a = true;
		boolean b = false;
		try{
			b = this.file.createNewFile();			
		}catch(Exception e){
			error("The file could no be created!", e);
		}
		
		return a && b;
	}

	/**
	 * Writes a new variable to the current line of JCode.
	 * @param key The name of the variable to save. This is used when loading the variable.
	 * @param value The value to save. This is saved by finding a valid parser using {@code JLineParsers.getParser(Object)}.
	 * @return True if a valid parser could be found. Note that if a perfect parser could not be found then a superclass parser 
	 * my be used. Check the output for more information.
	 */
	public boolean write(String key, Object value){
		// Check parsers
		
		JLineParser<?> parser = null;		
		try {
			parser = JLineParsers.getParser(value);
		} catch (JLIOException e) {
			error("No parser found!", e);
			System.exit(-1);
			return false;
		}
		
		parser.writeInternal(value, key.trim(), this);
		
		return true;		
	}
	
	/**
	 * Writes a new variable to the current line of JCode.
	 * Then creates a new blank line.
	 * @param key The name of the variable to save. This is used when loading the variable.
	 * @param value The value to save. This is saved by finding a valid parser using {@code JLineParsers.getParser(Object)}.
	 * @return True if a valid parser could be found. Note that if a perfect parser could not be found then a superclass parser 
	 * my be used. Check the output for more information.
	 */
	public boolean writeLine(String key, Object value){
		boolean worked = write(key, value);
		if(worked){
			newLine();
		}
		return worked;
	}
	
	/**
	 * Gets the current line number that variables are being written to.
	 */
	public int getLineNumber(){
		return lineNum;
	}

	/**
	 * Sets the current line number to an already existing line.
	 * @param index The line to move to.
	 */
	public void setLineNumber(int index){
		this.lineNum = index;
	}
	
	/**
	 * Gets the file that is being written to.
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Gets an internal StringBuilder.
	 */
	public StringBuilder getStringBuilder() {
		return stringBuilder;
	}
}
