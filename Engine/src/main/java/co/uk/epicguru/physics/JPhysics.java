package co.uk.epicguru.physics;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
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
	private static ArrayList<JCollisionData> tempCollision = new ArrayList<>();
	
	private static float PPM = 1;
	private static float dragsPerSecond = 60f;
	private static Vector2 defaultDrag = new Vector2(0.98f, 0.98f);
	private static Vector2 gravity = new Vector2();
	private static boolean collisions = true;
	
	
	protected static void print(Object object){
		//System.out.println(object == null ? "Null" : object.toString()); // Deploy
		Log.info("JPhysics", object == null ? "Null" : object.toString()); // Local
	}
	
	/**
	 * Resets all important world values :
	 * <li> Pixels per meter - {@link #getPPM()}
	 * <li> Gravity - {@link #getGravity()}
	 * <li> Collisions - {@link #getCollisions()}
	 * <li> DPS - {@link #getDragsPerSecond()}
	 * <li> Default Drag {@link #getDefaultDrag()}
	 */
	public static void reset(){
		setCollisions(true);
		setDefaultDrag(0.98f, 0.98f);
		setDragsPerSecond(60f);
		clearWorld();
		setPPM(1);
		setGravity(0, 0);
	}
	
	/**
	 * Adds a physics body to the active pool. This is done automatically by the default implementation of the physics body.
	 * This should only be called if you have a custom implementation that requires this to be called at a certain point.
	 * Note that if this is called, manually or through a constructor, within the update method of a physics object,
	 * then this new object will not be updated until next frame.
	 */
	public static void addBody(JPhysicsBody body){
		add.add(body);
	}
	
	/**
	 * Removes a body from the active pool. This will remove the object after the end of the update loop, and will only take effect after update.
	 */
	public static void removeBody(JPhysicsBody body){
		bin.add(body);
	}
	
	/**
	 * Updates the physics simulation. This calls update() and then updatePhysics() on all active objects.
	 * This also adds all pending bodies, added through {@link #addBody(JPhysicsBody)} and removes all unused bodies.
	 * @param delta The delta time value. This is the time, in seconds, between calls to this method. It is a good idea to use
	 * Gdx.graphics.getDeltaTime() for this.
	 */
	public static void update(float delta){
		addAll();
		sort();
		for(JPhysicsBody body : active){
			body.update(delta);
		}
		for(JPhysicsBody body : active){
			body.updatePhysics(delta);
		}
		clearBin();
	}
	
	/**
	 * Does a debug mode render of all bodies. Red bodies are static, grey bodies are normal.
	 * This calls the render() method on all bodies, and also temporarily end and restarts the batch.
	 * @param batch The Batch object, not used in rendering but is managed.
	 * @param camera The camera to render with. For the best results should be scaled in world units, see {@link #getPPM()}.
	 */
	public static void render(Batch batch, OrthographicCamera camera){
		batch.end();
		
		for(JPhysicsBody body : active){
			JPhysicsBody.setShapeColour(body.isStatic() ? Color.RED : Color.LIGHT_GRAY);
			body.render(camera);
		}
		
		batch.begin();
	}
	
	private static void sort(){
		Collections.sort(active);
	}
	
	/**
	 * Enables or disables collisions. See {@link #getCollisions()} for more info.
	 */
	public static void setCollisions(boolean flag){
		collisions = flag;
	}
	
	/**
	 * Checks to see if collisions are enabled.
	 * If collisions are disabled, it does not mean that they cannot happen.
	 * It means that they will not be triggered in {@link #update(float)}.
	 */
	public static boolean getCollisions(){
		return collisions;
	}
	
	/**
	 * Removes all bodies.
	 */
	public static void clearWorld(){
		active.clear();
		bin.clear();
		add.clear();
		tempCollision.clear();
	}
	
	private static void addAll(){
		for(JPhysicsBody body : add){
			active.add(body);
		}
		add.clear();
	}
	
	private static void clearBin(){
		for(JPhysicsBody body : bin){
			active.remove(body);
		}
		bin.clear();
	}
	
	/**
	 * Gets the default value for the drag field of all new JPhysicsBodies. This can be set through {@link #setDefaultDrag(float, float)} or 
	 * {@link #setDefaultDrag(Vector2)}.
	 */
	public static Vector2 getDefaultDrag(){
		return defaultDrag;
	}
	
	/**
	 * Sets the default drag value for all new bodies using the default implementation.
	 * @param drag The drag value. This is a multiplier, and therefore should be between 0 and 1. This value is applied
	 * {@link #getDragsPerSecond()} times per second to all active bodies.
	 */
	public static void setDefaultDrag(Vector2 drag){
		defaultDrag.set(drag);
	}
	
	/**
	 * Sets the default drag value for all new bodies using the default implementation.
	 * The parameters passed are the drag values. These is a multipliers, and therefore should be between 0 and 1. These values are applied
	 * {@link #getDragsPerSecond()} times per second to all active bodies.
	 */
	public static void setDefaultDrag(float x, float y){
		defaultDrag.set(x, y);
	}

	/**
	 * Gets all active bodies. DO NOT MODIFY!
	 */
	public static ArrayList<JPhysicsBody> getActiveBodies(){
		return active;
	}
	
	/**
	 * Gets all bodies ready to be activated. DO NOT MODIFY!
	 */
	public static ArrayList<JPhysicsBody> getPendingBodies(){
		return add;
	}
	
	/**
	 * Gets all dead bodies ready to be deactivated. DO NOT MODIFY!
	 */
	public static ArrayList<JPhysicsBody> getBinnedBodies(){
		return bin;
	}
	
	/**
	 * Gets all collisions that are occurring the the active bodies. This normally returns an array of 0 because all collisions are resolved internally.
	 * To detect collisions override a JPhysicsBody and implement the method collisionEvent().
	 * @param body The body to check for collisions with.
	 * @return An arraylist containing info about collisions. This always returns the same object, please read only.
	 */
	public static ArrayList<JCollisionData> getAllCollisions(JPhysicsBody body){
		tempCollision.clear();
		
		for(JPhysicsBody b : active){
			if(b != body){
				// Test for collision
				if(b.overlaps(body)){
					JCollisionData c = new JCollisionData();
					
					// Set data
					c.bodyA = body;
					c.bodyB = b;
					
					tempCollision.add(c);
				}
			}
		}	
		
		return tempCollision;
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
