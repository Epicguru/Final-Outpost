package co.uk.epicguru.IO.parsers.instances;

import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.IO.JLineWriter;
import co.uk.epicguru.IO.parsers.JLineParser;

public class ByteArrayParser extends JLineParser<byte[]> {

	public static StringBuilder stringBuilder = new StringBuilder();
	
	public ByteArrayParser() {
		super(byte[].class, "b\"");
	}

	public void write(byte[] object, String key, JLineWriter writer) {
		stringBuilder.setLength(0);
		
		int index = 0;
		for(byte b : object){
			stringBuilder.append(b);
			if(index != object.length - 1)
				stringBuilder.append(',');
			index++;
		}
		
		writer.writeRaw(stringBuilder.toString(), this, key);
	}

	@Override
	public byte[] read(String key, String content, JLineReader reader) {
		String[] array = content.split(",");
		
		byte[] array2 = new byte[array.length];
		int index = 0;
		for(String string : array){
			array2[index++] = Byte.parseByte(string);
		}
		
		return array2;
	}

}
