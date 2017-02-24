package co.uk.epicguru.parsers;

import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.IO.JLineWriter;
import co.uk.epicguru.IO.parsers.JLineParser;
import ro.fortsoft.pf4j.Extension;

@Extension
public class FloatParser extends JLineParser<Float> {

	public FloatParser() {
		super(Float.class, "f");
	}

	public void write(Float object, String key, JLineWriter writer) {
		writer.writeRaw(object.toString(), this, key);
	}

	public Float read(String key, String content, JLineReader reader) {
		return Float.parseFloat(content);
	}

}
