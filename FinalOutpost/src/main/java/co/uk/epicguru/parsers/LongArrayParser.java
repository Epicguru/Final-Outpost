package co.uk.epicguru.parsers;

import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.IO.JLineWriter;
import co.uk.epicguru.IO.parsers.JLineParser;
import ro.fortsoft.pf4j.Extension;

@Extension
public class LongArrayParser extends JLineParser<long[]> {

	public static StringBuilder stringBuilder = new StringBuilder();
	
	public LongArrayParser() {
		super(long[].class, "l\"");
	}

	public void write(long[] object, String key, JLineWriter writer) {
		stringBuilder.setLength(0);
		
		int index = 0;
		for(long l : object){
			stringBuilder.append(l);
			if(index != object.length - 1)
				stringBuilder.append(',');
			index++;
		}
		
		writer.writeRaw(stringBuilder.toString(), this, key);
	}

	@Override
	public long[] read(String key, String content, JLineReader reader) {
		String[] array = content.split(",");
		
		long[] array2 = new long[array.length];
		int index = 0;
		for(String string : array){
			array2[index++] = Long.parseLong(string);
		}
		
		return array2;
	}

}
