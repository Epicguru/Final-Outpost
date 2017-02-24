package co.uk.epicguru.parsers;

import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.IO.JLineWriter;
import co.uk.epicguru.IO.parsers.JLineParser;
import ro.fortsoft.pf4j.Extension;

@Extension
public class StringArrayParser extends JLineParser<String[]> {

	public static StringBuilder stringBuilder = new StringBuilder();
	
	public StringArrayParser() {
		super(String[].class, "s\"");
	}

	public void write(String[] object, String key, JLineWriter writer) {
		stringBuilder.setLength(0);
		
		int index = 0;
		for(String s : object){
			stringBuilder.append(s);
			if(index != object.length - 1)
				stringBuilder.append(',');
			index++;
		}
		
		writer.writeRaw(stringBuilder.toString(), this, key);
	}

	@Override
	public String[] read(String key, String content, JLineReader reader) {
		String[] array = content.split(",");
		return array;
	}

}
