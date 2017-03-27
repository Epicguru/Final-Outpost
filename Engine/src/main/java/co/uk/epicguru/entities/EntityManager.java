package co.uk.epicguru.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;

public class EntityManager {

	public static ArrayList<Entity> entities = new ArrayList<>();
	public static ArrayList<Entity> add = new ArrayList<>();
	public static ArrayList<Entity> bin = new ArrayList<>();
	
	public static void clear(){
		entities.clear();
	}

	public static void update(float delta){
		addPending();
		
		for(Entity e : entities){
			e.update(delta);
		}
		
		clearBin();
	}
	
	public static void render(float delta, Batch batch){
		for(Entity e : entities){
			e.render(delta, batch);
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
	
	public static void addEntity(Entity entity) {
		if(entity == null)
			return;
		add.add(entity);		
	}
	
	public static void removeEntity(Entity entity){
		if(entity == null)
			return;
		bin.add(entity);
	}
	
}
