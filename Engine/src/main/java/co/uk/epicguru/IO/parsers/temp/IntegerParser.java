package co.uk.epicguru.IO.parsers.temp;

import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.IO.JLineWriter;
import co.uk.epicguru.IO.parsers.JLineParser;

public class IntegerParser extends JLineParser<Integer> {

	public IntegerParser() {
		super(Integer.class, "i");
	}

	public void write(Integer object, String key, JLineWriter writer) {
		writer.writeRaw(object.toString(), this, key);
	}

	public Integer read(String key, String content, JLineReader reader) {
		return Integer.parseInt(content);
	}

}
