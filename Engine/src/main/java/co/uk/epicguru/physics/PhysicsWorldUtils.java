package co.uk.epicguru.physics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import co.uk.epicguru.main.FOE;

public final class PhysicsWorldUtils {

	public static final Vector2 gravity = new Vector2(0, 0);
	private static Box2DDebugRenderer debug;
	
	public static World newWorld(){
		removeWorld();
		
		PhysicsWorldUtils.debug = new Box2DDebugRenderer();
		
		return FOE.engine.setWorld(new World(gravity, true));
	}
	
	public static void removeWorld(){
		if(!sim()){
			return;
		}
		
		FOE.engine.getWorld().clearForces();
		FOE.engine.getWorld().dispose();
		FOE.engine.setWorld(null);
	}
	
	private static boolean sim(){
		return FOE.engine.getWorld() != null;
	}
	
	public static void update(float delta){
		if(!sim())
			return;
		
		FOE.engine.updateWorld(delta);
	}
	
	public static void render(Batch batch){		
		if(!sim())
			return;
		
		batch.end();		
		debug.render(FOE.engine.getWorld(), FOE.camera.combined);
		batch.begin();
	}

	public static void dispose() {
		debug = null;		
	}
}
