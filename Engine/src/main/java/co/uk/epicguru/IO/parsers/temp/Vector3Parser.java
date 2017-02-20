package co.uk.epicguru.IO.parsers.temp;

import com.badlogic.gdx.math.Vector3;

import co.uk.epicguru.IO.JLineReader;
import co.uk.epicguru.IO.JLineWriter;
import co.uk.epicguru.IO.parsers.JLineParser;

public class Vector3Parser extends JLineParser<Vector3> {

	public Vector3Parser() {
		super(Vector3.class, "V2");
	}

	public void write(Vector3 object, String key, JLineWriter writer) {
		float x = object.x;
		float y = object.y;
		float z = object.z;
		
		writer.writeRaw(x + "," + y + "," + z, this, key);
	}
	
	public Vector3 read(String key, String content, JLineReader reader) {
		String[] split = content.split(",");
		
		float x = Float.parseFloat(split[0].trim());
		float y = Float.parseFloat(split[1].trim());
		float z = Float.parseFloat(split[2].trim());
		
		return new Vector3(x, y, z);
	}
	
	
}
