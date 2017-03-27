package co.uk.epicguru.entities;

import com.badlogic.gdx.math.Vector2;

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
	}
	
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
