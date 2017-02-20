package co.uk.epicguru.IO.parsers.temp;

import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.IO.JLineWriter;
import co.uk.epicguru.IO.parsers.JLineParser;

public class FloatArrayParser extends JLineParser<float[]> {

	public static StringBuilder stringBuilder = new StringBuilder();
	
	public FloatArrayParser() {
		super(float[].class, "f\"");
	}

	public void write(float[] object, String key, JLineWriter writer) {
		stringBuilder.setLength(0);
		
		int index = 0;
		for(float f : object){
			stringBuilder.append(f);
			if(index != object.length - 1)
				stringBuilder.append(',');
			index++;
		}
		
		writer.writeRaw(stringBuilder.toString(), this, key);
	}

	@Override
	public float[] read(String key, String content, JLineReader reader) {
		String[] array = content.split(",");
		
		float[] array2 = new float[array.length];
		int index = 0;
		for(String string : array){
			array2[index++] = Float.parseFloat(string);
		}
		
		return array2;
	}

}
