package co.uk.epicguru.parsers;

import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.IO.JLineWriter;
import co.uk.epicguru.IO.parsers.JLineParser;
import ro.fortsoft.pf4j.Extension;

@Extension
public class LongParser extends JLineParser<Long> {

	public LongParser() {
		super(Long.class, "l");
	}

	public void write(Long object, String key, JLineWriter writer) {
		writer.writeRaw(object.toString(), this, key);
	}

	public Long read(String key, String content, JLineReader reader) {
		return Long.parseLong(content);
	}

}
