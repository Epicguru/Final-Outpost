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
	
	/**
	 * Sets the amount of pixels per in-game meter. A good size is 32.
	 * Generally there is no performance advantage of having any size. However rendering objects with a meter system is often simpler.
	 * Never set this while a physics simulation is running, as it can ruin scenes and create unstable or broken physics.
	 * @param PPM The pixels that are in each meter.
	 * @see {@link #getPPM()}.
	 */
	public void setPPM(float PPM){
		this.PPM = PPM;
	}
	
	/**
	 * Gets the amount of pixels in a in-game meter, as set in {@link #setPPM(float)}.
	 * The default value of this is 1.
	 * @return The pixels in one meter.
	 */
	public float getPPM(){
		return PPM;
	}
}
