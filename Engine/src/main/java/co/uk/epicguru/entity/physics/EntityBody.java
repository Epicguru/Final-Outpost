package co.uk.epicguru.entity.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import co.uk.epicguru.IO.NotSerialized;
import co.uk.epicguru.entity.Component;
import co.uk.epicguru.entity.Entity;
import co.uk.epicguru.main.FOE;

/**
 * An entity component that manages a Box2D body.
 * @author James Billy
 *
 */
public class EntityBody extends Component {

	public Vector2 offset = new Vector2();	
	@NotSerialized private Body body;
	
	@NotSerialized private boolean hasBeenLoaded;
	
	// For serialization only.
	private Vector2 position, velocity;
	private float angularVelocity, angle;
	
	public EntityBody() { }
	
	public EntityBody(Body body){
		this.setBody(body, false);
	}
	
	/**
	 * Destroys the box2D body, and sets the reference to null.
	 */
	public void destroyBody(){
		this.destroyBody(null);
	}
	
	/**
	 * Destroys the box2D body, and sets the reference to null.
	 * @param uponDestroyed Will be called when the body is removed from the world, which may be a few frames later.
	 */
	public void destroyBody(Runnable uponDestroyed){
		FOE.engine.removeBody(body, uponDestroyed);
		body = null;
	}
	
	/**
	 * Gets the currently stored body.
	 */
	public Body getBody(){
		return this.body;
	}
	
	/**
	 * Sets the current body.
	 * @param body The new body.
	 * @param destroyOld If true then the old body is removed from the world. If false it is simply abandoned.
	 * @return The old body for usage or cleanup.
	 */
	public Body setBody(Body body, boolean destroyOld){
		Body old = this.body;
		
		if(old != null && destroyOld){
			this.destroyBody();
		}
		
		this.body = body;
		if(body != null)
			body.setUserData(this); // TEST TODO MAKE SOMETHING USEFUL LIKE COLLISION.
		
		if(this.body != null && hasBeenLoaded){
			// We have been loaded and the body has been built again...
			// Now we need to apply the previous state!
			
			this.applyLoadedValues();
			hasBeenLoaded = false;
		}
		
		return old;
	}
	
	/**
	 * If the current body is not null, then this will move the entity to the position of the body.
	 * @param e The entity to move.
	 */
	public void update(Entity e){
		if(body == null)
			return;
		
		float offX = 0;
		if(offset != null) offX = offset.x;
		float offY = 0;
		if(offset != null) offY = offset.y;
		
		if(e.getPosition().x < 0)
			e.setPosition(0, e.getPosition().y);
		
		e.setPosition(body.getPosition().x + offX, body.getPosition().y + offY);
		
		if(!hasBeenLoaded){			
			position = body.getPosition();
			angle = body.getAngle();
			velocity = body.getLinearVelocity();
			angularVelocity = body.getAngularVelocity();
		}
	}
	
	public String toString(){
		return "Physics Body";
	}
	
	private void applyLoadedValues(){
		body.setTransform(position, angle);
		body.setLinearVelocity(velocity);
		body.setAngularVelocity(angularVelocity);
	}
	
	public void loaded(){
		if(body != null)
			applyLoadedValues();
		hasBeenLoaded = true;
	}
	
}
