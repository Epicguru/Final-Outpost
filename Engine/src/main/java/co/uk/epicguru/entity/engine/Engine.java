package co.uk.epicguru.entity.engine;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import co.uk.epicguru.entity.Entity;
import co.uk.epicguru.entity.Group;
import co.uk.epicguru.entity.physics.DeadBody;

public class Engine implements Disposable{

	private World world;
	
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private ArrayList<Entity> bin = new ArrayList<Entity>();
	private ArrayList<Entity> add = new ArrayList<Entity>();
	
	private ArrayList<DeadBody> bodyBin = new ArrayList<DeadBody>();
	
	private ArrayList<Entity> group = new ArrayList<Entity>();
	
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
		if(e != null)
			this.add.add(e);
	}
	
	public void remove(Entity e){
		if(e != null)
			this.bin.add(e);
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
	
	public void flushBodies(){
		
		if(this.getWorld().isLocked() || this.getWorld() == null)
			return;
		
		for(DeadBody b : bodyBin){
			if(b != null){
				if(b.body != null){
					this.getWorld().destroyBody(b.body);
					
					if(b.run != null){
						b.run.run();
					}
				}
			}			
		}
		bodyBin.clear();
	}
	
	public BodyDef newBodyDef(){
		BodyDef def = new BodyDef();
		def.linearDamping = 0.5f;
		return def;
	}
	
	public PolygonShape boxOfSize(float width, float height){
		
		// Make a polygon shape where the origin will be (0, 0) on the box. Good for rendering.
		PolygonShape shape = new PolygonShape();
		
		// Half width and height because Box2D likes to mess with you.
		shape.setAsBox(width / 2f, height / 2f, new Vector2(width / 2f,  height / 2f), 0);
		
		return shape;
	}
	
	public Body newBody(Entity e, BodyDef def){
		if(this.getWorld() == null)
			return null;
		
		Body b = this.getWorld().createBody(def);
		b.setUserData(e);
		return b;
	}
	
	public void disposeWorld(){
		if(this.getWorld() == null)
			return;
		
		this.getWorld().dispose();
		
		this.setWorld(null);
	}
	
	public void removeBody(Body body, Runnable run){
		this.bodyBin.add(new DeadBody(body, run));
	}
	
	public void dispose(){
		this.clearEntities();
		this.flushBodies();
		this.disposeWorld();
	}
}
