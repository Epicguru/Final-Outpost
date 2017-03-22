package co.uk.epicguru.physics;

import co.uk.epicguru.logging.Log;

/**
 * A simple rectangle based physics engine. Supports (TODO) momentum, collision callback and more.
 * @author James Billy (Team Epicguru)
 *
 */
public class JPhysics {
	
	private float PPM = 1;
	
	protected static void print(Object object){
		//System.out.println(object == null ? "Null" : object.toString()); // Deploy
		Log.info("JPhysics", object == null ? "Null" : object.toString()); // Local
	}
	
	public void setPPM(float PPM){
		this.PPM = PPM;
	}
	
	public float getPPM(){
		return PPM;
	}
}
