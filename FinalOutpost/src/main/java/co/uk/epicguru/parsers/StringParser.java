package co.uk.epicguru.parsers;

import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.IO.JLineWriter;
import co.uk.epicguru.IO.parsers.JLineParser;
import ro.fortsoft.pf4j.Extension;

@Extension
public class StringParser extends JLineParser<String>{

	public StringParser() {
		super(String.class, "s");
	}

	public void write(String object, String key, JLineWriter writer) {
		writer.writeRaw(object, this, key);
	}

	public String read(String key, String content, JLineReader reader) {
		return content;
	}

}
