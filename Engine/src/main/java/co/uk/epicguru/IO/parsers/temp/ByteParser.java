package co.uk.epicguru.IO.parsers.temp;

import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.IO.JLineWriter;
import co.uk.epicguru.IO.parsers.JLineParser;

public class ByteParser extends JLineParser<Byte>{

	public ByteParser() {
		super(Byte.class, "b");
	}

	public void write(Byte object, String key, JLineWriter writer) {
		writer.writeRaw(object.toString(), this, key);
	}

	public Byte read(String key, String content, JLineReader reader) {
		return Byte.parseByte(content);
	}

}
