package co.uk.epicguru.entity.engine;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import box2dLight.RayHandler;
import co.uk.epicguru.API.Base;
import co.uk.epicguru.IO.JIO;
import co.uk.epicguru.entity.Component;
import co.uk.epicguru.entity.Entity;
import co.uk.epicguru.entity.Group;
import co.uk.epicguru.entity.physics.DeadBody;
import co.uk.epicguru.logging.Log;
import co.uk.epicguru.main.FOE;
import co.uk.epicguru.map.TiledMap;
import co.uk.epicguru.physics.PhysicsWorldUtils;

public class Engine extends Base implements Disposable{

	private static final String TAG = "Entity Engine";
	
	private World world;
	private Splitter splitter;
	
	private RayHandler rays;	
	private int RAYS_PER_LIGHT;
	private float LIGHT_RESOLUTION_SCALE;
	private int LIGHT_BLUR_PASSES;
	
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private ArrayList<Entity> bin = new ArrayList<Entity>();
	private ArrayList<Entity> add = new ArrayList<Entity>();
	
	private ArrayList<DeadBody> bodyBin = new ArrayList<DeadBody>();
	
	private ArrayList<Entity> group = new ArrayList<Entity>();
	
	/**
	 * Gets the Box2D world.
	 */
	public World getWorld(){
		return this.world;
	}
	
	/**
	 * Gets the ray handler that make lights work!
	 */
	public RayHandler getRayHandler(){
		return this.rays;
	}
	
	/**
	 * Sets the ray handler. Please do not use this!
	 */
	public void setRayHandler(RayHandler ray){
		this.rays = ray;
	}
	
	public int getRaysPerLight(){
		return RAYS_PER_LIGHT;
	}
	
	public void setRaysPerLight(int rays){
		this.RAYS_PER_LIGHT = rays;
	}
	
	public float getLightResolutionScale(){
		return LIGHT_RESOLUTION_SCALE;
	}
	
	public void setLightResolutionScale(float scale){
		this.LIGHT_RESOLUTION_SCALE = scale;
	}
	
	public int getLightBlurPasses(){
		return LIGHT_BLUR_PASSES;
	}
	
	public void setLightBlurPasses(int passes){
		LIGHT_BLUR_PASSES = passes;
	}
	
	public Splitter getSplitter(){
		return this.splitter;
	}

	/**
	 * Sets the Box2D world. Please do not use this!
	 */
	public World setWorld(World world) {
		return this.world = world;
	}
	
	public void setTiledMap(TiledMap map){
		this.splitter = new Splitter(this);
		splitter.setMap(map);		
	}
	
	/**
	 * Gets all entities of a particular {@link Group}. This ALWAYS returns the same array list so please
	 * do not keep the array, just read it and leave it! (I'm a poet and I know it).
	 * @param group The Group to get entities for.
	 * @return An ArrayList of entities that match the criteria.
	 */
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
	
	/**
	 * Gets all entities of a particular {@link Group}. This creates a new array and so is safe to store unlike {@link #ofGroup(Group)}.
	 * @param group The Group to get entities for.
	 * @return An array of entities that match the criteria.
	 */
	public Entity[] ofGroupArray(Group group){
		return this.ofGroupArray(group, new Entity[0]);
	}
	
	/**
	 * Gets all entities of a particular {@link Group}. The entities are packed into the array specified and so is safe to store unlike {@link #ofGroup(Group)}.
	 * @param group The Group to get entities for.
	 * @return An array of entities that match the criteria.
	 */
	public Entity[] ofGroupArray(Group group, Entity[] array){
		ArrayList<Entity> e = this.ofGroup(group);
		return e == null ? null : e.toArray(array);
	}
	
	/**
	 * Removes all entities from the world.
	 */
	public void clearEntities(){
		this.add.clear();
		for(Entity e : entities){
			bin.add(e);
		}
		this.flush();
		if(this.splitter != null){
			this.splitter.clearEntities();
		}
	}

	/**
	 * Adds an entity to this Engine.
	 * @param e The entity to add, if null ignored.
	 */
	public void add(Entity e){
		if(e != null)
			this.add.add(e);
	}
	
	/**
	 * Removes an entity from the Engine.
	 * @param e The entity to add, if null ignored.
	 */
	public void remove(Entity e){
		if(e != null)
			this.bin.add(e);
	}
	
	public void saveEntities(){
		// Step 1: Put all entities in a data structure. EDIT - Just use the arraylist.
		// Step 2: Serialize
		// Step 3: Save to file.
		
		ArrayList<Entity> real = new ArrayList<Entity>();
		
		for(Entity e : entities){
			if(e.doesSerialize())
				real.add(e);
		}
		
		String serialized = JIO.toJson(real, false);
		
		File file = new File(FOE.gameDirectory + "Testing/" + "Entities.txt");
		
		try {
			FileUtils.write(file, serialized, Charset.defaultCharset());
		} catch (IOException e) {
			Log.error(TAG, "Error saving entities!", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadEntities(){
		this.clearEntities();
		
		File file = new File(FOE.gameDirectory + "Testing/" + "Entities.txt");
		String data = null;
		try {
			data = FileUtils.readFileToString(file, Charset.defaultCharset());
		} catch (IOException e) {
			Log.error(TAG, "Error loading entities!", e);
			return;
		}		
		
		ArrayList<Entity> newEntities = new ArrayList<Entity>();
		newEntities = (ArrayList<Entity>)JIO.fromJson(data, newEntities.getClass());	
		
		this.add = newEntities;
		
		// Tell components that they were loaded.
		for(Entity e : this.add){
			for(Component c : e.getComponents()){
				c.loaded();
			}
		}		
		
		this.addNew(true);
		this.flush();		
		if(this.splitter != null){
			this.splitter.placeEntities();
		}
	}
	
	protected void addNew(boolean loaded){
		for(Entity e : add){
			entities.add(e);
			e.added(loaded);
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

	/**
	 * Updates the Box2D world that this Engine manages. See {@link #getWorld()}.
	 * @param delta The delta-time value.
	 */
	public void updateWorld(float delta){
		if(this.getWorld() == null)
			return;
		
		this.getWorld().step(delta, 8, 3);
	}
	
	/**
	 * Gets all active entities. DO NOT MANIPULATE ARRAY!!!!
	 */
	public ArrayList<Entity> getAllEntities(){
		return entities;
	}
	
	/**
	 * Updates the entities in this engine.
	 * @param delta The delta-time value.
	 */
	public void update(float delta){
		this.addNew(false);
		this.flush();
		
		for(Entity e : entities){
			e.update(delta);
		}
		
		this.flush();
		
		if(this.splitter != null){
			this.splitter.clearEntities();
			this.splitter.placeEntities();
		}
	}
	
	/**
	 * Updates the entities in this engine.
	 * @param batch The batch to render entities with.
	 * @param delta The delta-time value.
	 */
	public void render(Batch batch, float delta){
		for(Entity e : entities){
			e.render(batch, delta);
		}
		
		this.flush();
	}
	
	/**
	 * Renders UI elements of the entities in this engine.
	 * @param delta The delta-time value.
	 */
	public void renderUI(Batch batch, float delta){
		for(Entity e : entities){
			e.renderUI(batch, delta);
		}
		
		this.flush();
	}
	
	public void resize(int width, int height){

		int FBOW = (int)(width / this.getLightResolutionScale());
		int FBOH = (int)(height / this.getLightResolutionScale());
		print("Resizing to " + width + "x" + height + "px to give " + FBOW + "x" + FBOH + "px FBO @ " + this.getLightResolutionScale() + " RS.");
		
		if(this.getRayHandler() != null){
			this.getRayHandler().resizeFBO(FBOW, FBOH);
		}
	}
	
	/**
	 * Renders Box2D lights, if the world is not null.
	 * This method stops and restarts the batch.
	 */
	public void renderLights(Batch batch, float delta){
		
		if(this.getWorld() == null)
			return;
		
		batch.end();
		
		this.getRayHandler().setBlurNum(this.getLightBlurPasses());
		this.getRayHandler().setWorld(this.getWorld());
		this.getRayHandler().setCombinedMatrix(FOE.camera);
		this.getRayHandler().updateAndRender();
		
		batch.begin();
	}
	
	/**
	 * Internal.
	 */
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
	
	/**
	 * Creates a new BodyDef where the linear dampening is set to 0.5 .
	 */
	public BodyDef newBodyDef(){
		BodyDef def = new BodyDef();
		def.linearDamping = 0.5f;
		return def;
	}
	
	/**
	 * Creates a new PolygonShape where the origin is (0, 0) and the width and height are measured in tiles.
	 */
	public PolygonShape boxOfSize(float width, float height){
		
		// Make a polygon shape where the origin will be (0, 0) on the box. Good for rendering.
		PolygonShape shape = new PolygonShape();
		
		// Half width and height because Box2D likes to mess with you.
		shape.setAsBox(width / 2f, height / 2f, new Vector2(width / 2f,  height / 2f), 0);
		
		return shape;
	}
	
	/**
	 * Creates a new Box2D body and adds it to the physics world.
	 * @param e The Entity to associate the body to.
	 * @param def The BodyDef to base the body off.
	 * @return The new Body.
	 */
	public Body newBody(Entity e, BodyDef def){
		if(this.getWorld() == null)
			return null;
		
		Body b = this.getWorld().createBody(def);
		b.setUserData(e);
		return b;
	}
	
	/**
	 * Disposes the Box2D world.
	 */
	public void disposeWorld(){
		if(this.getWorld() == null)
			return;
		
		this.getWorld().dispose();
		
		this.setWorld(null);
	}
	
	/**
	 * Safely removes a Box2D body from the world.
	 * @param body The body to remove.
	 * @param run The optional runnable object that will be called once the body is removed, as it may take a frame or two to remove
	 * it properly.
	 */
	public void removeBody(Body body, Runnable run){
		this.bodyBin.add(new DeadBody(body, run));
	}
	
	/**
	 * Disposes the whole Engine.
	 */
	public void dispose(){
		this.flushBodies();
		PhysicsWorldUtils.removeWorld();
		PhysicsWorldUtils.dispose();
		this.clearEntities();
		splitter.clearEntities();
		splitter = null;
		if(this.getRayHandler() != null)
			this.getRayHandler().dispose();
		this.disposeWorld();
	}
}
