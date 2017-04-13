package co.uk.epicguru.entity.engine;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;

import co.uk.epicguru.entity.Entity;
import co.uk.epicguru.entity.Group;

public class Engine {

	private World world;
	
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private ArrayList<Entity> bin = new ArrayList<Entity>();
	private ArrayList<Entity> add = new ArrayList<Entity>();
	
	private ArrayList<Entity> group = new ArrayList<Entity>();
	
	public Engine(World world){
		this.world = world;
	}
	
	public World getWorld(){
		return this.world;
	}
	
	public World setWorld(World world){
		return this.world = world;
	}
	
	public ArrayList<Entity> ofGroup(Group group){
		if(group == null){
			return null;
		}
		
		this.group.clear();
		for(Entity e : entities){
			if(group.isOfGroup(e)){
				this.group.add(e);
			}
		}
		
		return this.group;
	}
	
	public Entity[] ofGroupArray(Group group){
		return this.ofGroupArray(group, new Entity[0]);
	}
	
	public Entity[] ofGroupArray(Group group, Entity[] array){
		ArrayList<Entity> e = this.ofGroup(group);
		return e == null ? null : e.toArray(array);
	}
	
	public void clearEntities(){
		this.addNew();
		for(Entity e : entities){
			bin.add(e);
		}
		flush();	
	}
	
	public void add(Entity e){
		this.add.add(e);
	}
	
	protected void addNew(){
		for(Entity e : add){
			entities.add(e);
			e.added();
		}
		add.clear();
	}
	
	protected void flush(){
		for(Entity e : bin){
			entities.remove(e);
			e.removed();
		}
		bin.clear();
	}

	public void updateWorld(float delta){
		if(this.getWorld() == null)
			return;
		
		this.getWorld().step(delta, 8, 3);
	}
	
	public ArrayList<Entity> getAllEntities(){
		return entities;
	}
	
	public void update(float delta){
		this.addNew();
		this.flush();
		
		for(Entity e : entities){
			e.update(delta);
		}
		
		this.flush();
	}
	
	public void render(Batch batch, float delta){
		for(Entity e : entities){
			e.render(batch, delta);
		}
		
		this.flush();
	}
	
	public void renderUI(Batch batch, float delta){
		for(Entity e : entities){
			e.renderUI(batch, delta);
		}
		
		this.flush();
	}
}
