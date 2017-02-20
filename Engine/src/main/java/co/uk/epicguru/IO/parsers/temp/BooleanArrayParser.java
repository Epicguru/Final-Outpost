package co.uk.epicguru.IO.parsers.temp;

import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.IO.JLineWriter;
import co.uk.epicguru.IO.parsers.JLineParser;

public class BooleanArrayParser extends JLineParser<boolean[]> {

	public static StringBuilder stringBuilder = new StringBuilder();
	
	public BooleanArrayParser() {
		super(boolean[].class, "B\"");
	}

	public void write(boolean[] object, String key, JLineWriter writer) {
		stringBuilder.setLength(0);
		
		int index = 0;
		for(boolean b : object){
			stringBuilder.append(b);
			if(index != object.length - 1)
				stringBuilder.append(',');
			index++;
		}
		
		writer.writeRaw(stringBuilder.toString(), this, key);
	}

	@Override
	public boolean[] read(String key, String content, JLineReader reader) {
		String[] array = content.split(",");
		
		boolean[] array2 = new boolean[array.length];
		int index = 0;
		for(String string : array){
			array2[index++] = Boolean.parseBoolean(string);
		}
		
		return array2;
	}

}
