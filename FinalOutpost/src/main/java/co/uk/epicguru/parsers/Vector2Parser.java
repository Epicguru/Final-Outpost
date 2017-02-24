package co.uk.epicguru.parsers;

import com.badlogic.gdx.math.Vector2;

import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.IO.JLineWriter;
import co.uk.epicguru.IO.parsers.JLineParser;
import ro.fortsoft.pf4j.Extension;

@Extension
public class Vector2Parser extends JLineParser<Vector2> {

	public Vector2Parser() {
		super(Vector2.class, "V2");
	}

	public void write(Vector2 object, String key, JLineWriter writer) {
		float x = object.x;
		float y = object.y;
		
		writer.writeRaw(x + "," + y, this, key);
	}
	
	public Vector2 read(String key, String content, JLineReader reader) {
		String[] split = content.split(",");
		
		float x = Float.parseFloat(split[0].trim());
		float y = Float.parseFloat(split[1].trim());
		
		return new Vector2(x, y);
	}
	
	
}
