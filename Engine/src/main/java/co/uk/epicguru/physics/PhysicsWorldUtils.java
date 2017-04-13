package co.uk.epicguru.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import co.uk.epicguru.main.FOE;

public final class PhysicsWorldUtils {

	public static final Vector2 gravity = new Vector2(0, 0);
	private static Box2DDebugRenderer debug = new Box2DDebugRenderer();
	
	public static World newWorld(){
		removeWorld();
		
		return FOE.physicsWorld = new World(gravity, true);
	}
	
	public static void removeWorld(){
		if(FOE.physicsWorld == null){
			return;
		}
		
		FOE.physicsWorld.clearForces();
		FOE.physicsWorld.dispose();
		FOE.physicsWorld = null;
	}
	
	private static boolean sim(){
		return FOE.physicsWorld != null;
	}
	
	public static void update(float delta){
		if(!sim())
			return;
		
		FOE.physicsWorld.step(delta, 8, 3);
	}
	
	public static void render(){		
		if(!sim())
			return;
		
		debug.render(FOE.physicsWorld, FOE.camera.combined);
	}
}
