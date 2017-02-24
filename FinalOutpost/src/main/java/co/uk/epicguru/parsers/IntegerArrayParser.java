package co.uk.epicguru.parsers;

import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.IO.JLineWriter;
import co.uk.epicguru.IO.parsers.JLineParser;
import ro.fortsoft.pf4j.Extension;

@Extension
public class IntegerArrayParser extends JLineParser<int[]> {

	public static StringBuilder stringBuilder = new StringBuilder();
	
	public IntegerArrayParser() {
		super(int[].class, "i\"");
	}

	public void write(int[] object, String key, JLineWriter writer) {
		stringBuilder.setLength(0);
		
		int index = 0;
		for(int i : object){
			stringBuilder.append(i);
			if(index != object.length - 1)
				stringBuilder.append(',');
			index++;
		}
		
		writer.writeRaw(stringBuilder.toString(), this, key);
	}

	@Override
	public int[] read(String key, String content, JLineReader reader) {
		String[] array = content.split(",");
		
		int[] array2 = new int[array.length];
		int index = 0;
		for(String string : array){
			array2[index++] = Integer.parseInt(string);
		}
		
		return array2;
	}

}
