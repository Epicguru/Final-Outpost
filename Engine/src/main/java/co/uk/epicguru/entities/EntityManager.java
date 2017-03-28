package co.uk.epicguru.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;

public class EntityManager {

	private static ArrayList<Entity> entities = new ArrayList<>();
	private static ArrayList<Entity> add = new ArrayList<>();
	private static ArrayList<Entity> bin = new ArrayList<>();
	
	/**
	 * Clears all entities from the active list.
	 */
	public static void clear(){
		entities.clear();
		add.clear();
		bin.clear();
	}

	/**
	 * Updates all active entities.
	 * @param delta The delta time value.
	 */
	public static void update(float delta){
		addPending();
		
		for(Entity e : entities){
			e.update(delta);
		}
		
		clearBin();
	}
	
	/**
	 * Renders all active entities.
	 * @param delta The delta time value.
	 * @param batch The batch to draw with.
	 */
	public static void render(float delta, Batch batch){
		for(Entity e : entities){
			e.render(delta, batch);
		}
		
		clearBin();
	}
	
	/**
	 * Renders the UI for all active entities.
	 * @param delta The delta time value.
	 * @param batch The batch to draw with.
	 */
	public static void renderUI(float delta, Batch batch){
		for(Entity e : entities){
			e.renderUI(delta, batch);
		}
		
		clearBin();
	}
	
	private static void addPending(){
		for(Entity e : add){
			entities.add(e);
		}
		add.clear();
	}
	
	private static void clearBin(){
		for(Entity e : bin){
			entities.remove(e);
		}
		bin.clear();
	}
	
	/**
	 * Gets the array list of active entities. DO NOT MODIFY!
	 */
	public static ArrayList<Entity> getActiveEntities(){
		return entities;
	}
	
	/**
	 * Adds an entity to the pending buffer, which is then added to the active list next frame.
	 * There is no need to call this normally as the default constructor will add the entity by default.
	 * @param entity The entity to add, ignored if null.
	 */
	public static void addEntity(Entity entity) {
		if(entity == null)
			return;
		add.add(entity);		
	}
	
	/**
	 * Removes an entity from the world, equivalent to the entity's destroy() method.
	 * @param entity The entity to remove, ignored if null.
	 */
	public static void removeEntity(Entity entity){
		if(entity == null)
			return;
		bin.add(entity);
	}
	
}
