package co.uk.epicguru.IO.parsers.temp;

import com.badlogic.gdx.graphics.Color;

import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.IO.JLineWriter;
import co.uk.epicguru.IO.parsers.JLineParser;

public class ColorParser extends JLineParser<Color> {

	private static StringBuilder stringBuilder = new StringBuilder();
	
	public ColorParser() {
		super(Color.class, "Cl");
	}

	public void write(Color object, String key, JLineWriter writer) {
		
		float r = object.r;
		float g = object.g;
		float b = object.b;
		float a = object.a;
		
		stringBuilder.setLength(0);
		stringBuilder.append(r);
		stringBuilder.append(',');
		stringBuilder.append(g);
		stringBuilder.append(',');
		stringBuilder.append(b);
		stringBuilder.append(',');
		stringBuilder.append(a);
		
		writer.writeRaw(stringBuilder.toString(), this, key);
	}

	public Color read(String key, String content, JLineReader reader) {
		String[] values = content.split(",");
		float r = Float.parseFloat(values[0].trim());
		float g = Float.parseFloat(values[1].trim());
		float b = Float.parseFloat(values[2].trim());
		float a = Float.parseFloat(values[3].trim());
		
		return new Color(r, g, b, a);
	}
}
