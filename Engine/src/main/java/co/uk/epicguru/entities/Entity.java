package co.uk.epicguru.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import co.uk.epicguru.API.AllocatedTimer;
import co.uk.epicguru.API.Allocator;
import co.uk.epicguru.API.Base;

/**
 * A class that represents an object that operates in the game world that updates, renders and has a location within the map.
 * This can be anything from players to projectiles.
 * @author James Billy
 */
public abstract class Entity extends Base {

	private static Vector2 fakeVector = new Vector2();
	private Vector2 position = new Vector2();
	private String name;
	private AllocatedTimer inputTimer;
	
	/**
	 * Default no-param constructor that leaves name as default value. Position will be 0, 0.
	 */
	public Entity(){
		this("Default Entity Name");
	}
	
	/**
	 * Starts the entity with a name, and a default position of 0, 0.
	 * @param name The user friendly name for this entity.
	 */
	public Entity(String name){
		this(0, 0, name);
	}
	
	/**
	 * Starts the entity with a position and a name.
	 * @param position The starting coordinates of the entity. In tiles.
	 * @param name The user friendly name for this entity.
	 */
	public Entity(Vector2 position, String name){
		this(position.x, position.y, name);
	}
	
	/**
	 * Starts the entity with a position and a name.
	 * @param x The X coordinate in tiles.
	 * @param y The Y coordinate in tiles.
	 * @param name The user friendly name for this entity.
	 */
	public Entity(float x, float y, String name){
		this.position = new Vector2(x, y);
		this.name = name;
		this.addToWorld();
	}
	
	/**
	 * Offsets this entity's position by the parameters.
	 */
	public void translate(Vector2 offset){
		if(offset == null)
			return;
		
		this.translate(offset.x, offset.y);
	}
	
	/**
	 * Offsets this entity's position by the parameters.
	 */
	public void translate(float xOffset, float yOffset){
		this.setPosition(this.getPosition().x + xOffset, this.getPosition().y + yOffset);
	}
	
	/**
	 * Sets the position of this entity.
	 * @param position The new position of this entity, in tiles.
	 */
	public void setPosition(Vector2 position){
		if(position == null)
			return;
		
		this.setPosition(position.x, position.y);
	}
	
	/**
	 * Sets the position of this entity.
	 * @param x The X coordinate in tiles.
	 * @param y The Y coordinate in tiles.
	 */
	public void setPosition(float x, float y){
		this.position.set(x, y);
	}
	
	/**
	 * Starts an {@link AllocatedTimer} that calls {@link #input()} <code> timesPerSecond </code> times per second.
	 * Works in a separate thread, so no GL stuff please!
	 */
	protected void startInput(float timesPerSecond){
		if(inputTimer != null){
			inputTimer.setTPM(timesPerSecond / 60f);
		}else{
			inputTimer = Allocator.add(AllocatedTimer.inSecond(timesPerSecond, () -> {
				input();
			}));
		}
	}
	
	/**
	 * Stops the input timer, if it was started.
	 */
	protected void stopInput(){
		Allocator.removeTimer(inputTimer);
		inputTimer = null;
	}
	
	/**
	 * Adds this entity to the main list where it is managed by {@link EntityManager}.
	 * Called in the default constructors.
	 */
	public void addToWorld(){
		EntityManager.addEntity(this);
	}
	
	/**
	 * Removes this entity from the managed list.
	 */
	public void destroy(){
		EntityManager.removeEntity(this);
	}
	
	/**
	 * Called only if the input timer is activated using {@link #startInput(float)}.
	 * It is made for framerate independent input, but is can be used for anything. This method will call in a thread separate from
	 * the renderer one. Default implementation does nothing.
	 */
	public void input(){ }
	
	/**
	 * Called once per frame, do all logic here. Default implementation does nothing.
	 * @param delta The delta time value.
	 */
	public void update(float delta){ }
	
	/**
	 * Called once per frame, render the entity here. Default implementation does nothing.
	 * @param delta The delta time value.
	 * @param batch The batch to draw with. Use FOE.camera for a camera.
	 * @see {@link #renderUI(float, Batch)} to render UI elements.
	 */
	public void render(float delta, Batch batch){ }
	
	/**
	 * Called once per frame, differs from {@link #render(float, Batch)} in that the batch is
	 * scaled to render UI elements. Default implementation does nothing.
	 * @param delta The delta time value.
	 * @param batch The batch to draw with. Use FOE.UIcamera for a camera.
	 */
	public void renderUI(float delta, Batch batch){ }
	
	/**
	 * Gets the position of the object within the world map. This value is in tiles.
	 * @return A 'fake' vector meaning that modifying it will not move this entity.
	 */
	public Vector2 getPosition(){
		return fakeVector.set(this.position);
	}
	
	/**
	 * Gets the X position of this object. This is in tiles.
	 * @see {@link #getPosition()} to get the whole position.
	 */
	public float getX(){
		return this.position.x;
	}
	
	/**
	 * Gets the Y position of this object. This is in tiles.
	 * @see {@link #getPosition()} to get the whole position.
	 */
	public float getY(){
		return this.position.y;
	}
	
	/**
	 * Gets the user friendly name for this entity.
	 * This does not have to be unique for every entity.
	 * @return The name as set by the entity owner.
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Gets the name of the java class that this object runs from.
	 */
	public final String getClassName(){
		return this.getClass().getName();
	}
	
	public String toString(){	
		return '[' + this.getClassName() + "] " + this.getName() + " " + this.getPosition();
	}
}
