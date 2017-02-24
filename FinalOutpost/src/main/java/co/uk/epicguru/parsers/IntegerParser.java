package co.uk.epicguru.parsers;

import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.IO.JLineWriter;
import co.uk.epicguru.IO.parsers.JLineParser;
import ro.fortsoft.pf4j.Extension;

@Extension
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
