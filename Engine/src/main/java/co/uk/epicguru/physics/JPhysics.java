package co.uk.epicguru.physics;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import co.uk.epicguru.logging.Log;

/**
 * A simple rectangle based physics engine. Supports (TODO) momentum, collision callback and more.
 * @author James Billy (Team Epicguru)
 *
 */
public class JPhysics {
	
	/**
	 * The gravity on planet earth. Use in {@link #setGravity(Vector2)}.
	 */
	public static final Vector2 GRAVITY_EARTH = new Vector2(0, -9.807f);
	/**
	 * The gravity on earth's moon. Use in {@link #setGravity(Vector2)}.
	 */
	public static final Vector2 GRAVITY_MOON = new Vector2(0, -1.622f);
	/**
	 * Zero gravity, outer space. Use in {@link #setGravity(Vector2)}.
	 */
	public static final Vector2 GRAVITY_ZERO = new Vector2(0, 0);
	
	private static ArrayList<JPhysicsBody> active = new ArrayList<>();
	private static ArrayList<JPhysicsBody> bin = new ArrayList<>();
	private static ArrayList<JPhysicsBody> add = new ArrayList<>();
	
	private static float PPM = 1;
	private static float dragsPerSecond = 60f;
	private static Vector2 defaultDrag = new Vector2(0.98f, 0.98f);
	private static Vector2 gravity = new Vector2();
	
	
	protected static void print(Object object){
		//System.out.println(object == null ? "Null" : object.toString()); // Deploy
		Log.info("JPhysics", object == null ? "Null" : object.toString()); // Local
	}
	
	/**
	 * Resets all important world values :
	 * <li> Pixels per meter - {@link #getPPM()}
	 * <li> Gravity - {@link #getGravity()}
	 */
	public static void reset(){
		clearWorld();
		setPPM(1);
		setGravity(0, 0);
	}
	
	public static void addBody(JPhysicsBody body){
		add.add(body);
	}
	
	public static void removeBody(JPhysicsBody body){
		bin.add(body);
	}
	
	public static void update(float delta){
		addAll();
		for(JPhysicsBody body : active){
			body.update(delta);
		}
		clearBin();
	}
	
	public static void clearWorld(){
		active.clear();
	}
	
	private static void addAll(){
		for(JPhysicsBody body : add){
			active.add(body);
		}
	}
	
	private static void clearBin(){
		for(JPhysicsBody body : bin){
			active.remove(body);
		}
		bin.clear();
	}
	
	public static Vector2 getDefaultDrag(){
		return defaultDrag;
	}
	
	public static void setDefaultDrag(Vector2 drag){
		defaultDrag.set(drag);
	}
	
	public static void setDefaultDrag(float x, float y){
		defaultDrag.set(x, y);
	}
	
	/**
	 * Gets the value that represents how many times, per second (where a second is determined by the deltaTime value),
	 * that the drag multiplier is applied to all active physics bodies. If you are unsure that this is then leave it alone!
	 * The default value is 60;
	 */
	public static float getDragsPerSecond(){
		return dragsPerSecond;
	}
	
	/**
	 * Sets the times per second that the drag multiplier is applied to all active bodies. See {@link #getDragsPerSecond()} for more info.
	 */
	public static void setDragsPerSecond(float dragsPerSecond){
		JPhysics.dragsPerSecond = dragsPerSecond;
	}
	
	/**
	 * Sets the amount of pixels per in-game meter. A good size is 32.
	 * Generally there is no performance advantage of having any size. However rendering objects with a meter system is often simpler.
	 * Never set this while a physics simulation is running, as it can ruin scenes and create unstable or broken physics.
	 * @param PPM The pixels that are in each meter.
	 * @see {@link #getPPM()}.
	 */
	public static void setPPM(float PPM){
		JPhysics.PPM = PPM;
	}
	
	/**
	 * Gets the amount of pixels in a in-game meter, as set in {@link #setPPM(float)}.
	 * The default value of this is 1.
	 * @return The pixels in one meter.
	 */
	public static float getPPM(){
		return PPM;
	}
	
	/**
	 * Sets the gravity that will be applied every update to all physics bodies.
	 * Gravity in JPhysics is the force applied over a second, in world units.
	 * A negative Y will make objects fall towards the bottom of the screen, on a Y down setup.
	 * @param gravity The gravity to set.
	 */
	public static void setGravity(Vector2 gravity){
		if(gravity == null){
			throw new IllegalArgumentException("Gravity value cannot be null!");
		}
		JPhysics.gravity.set(gravity);
	}
	
	/**
	 * Sets the gravity that will be applied every update to all physics bodies.
	 * Gravity in JPhysics is the force applied over a second, in world units.
	 * A negative Y will make objects fall towards the bottom of the screen, on a Y down setup.
	 */
	public static void setGravity(float x, float y){
		gravity.set(x, y);
	}
	
	/**
	 * Gets the current value of gravity in all JPhysics simulations.
	 * @see {@link #setGravity(Vector2)}, {@link #setGravity(float, float)}.
	 */
	public static Vector2 getGravity(){
		return gravity;
	}
}
