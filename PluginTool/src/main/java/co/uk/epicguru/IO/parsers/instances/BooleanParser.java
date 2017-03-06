package co.uk.epicguru.IO.parsers.instances;

import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.IO.JLineWriter;
import co.uk.epicguru.IO.parsers.JLineParser;

public class BooleanParser extends JLineParser<Boolean> {

	public BooleanParser() {
		super(Boolean.class, "B");
	}

	public void write(Boolean object, String key, JLineWriter writer) {
		writer.writeRaw(object.toString(), this, key);
	}

	public Boolean read(String key, String content, JLineReader reader) {
		return Boolean.parseBoolean(content);
	}
}
