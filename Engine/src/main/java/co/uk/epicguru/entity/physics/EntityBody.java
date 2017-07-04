package co.uk.epicguru.entity.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import co.uk.epicguru.entity.Component;
import co.uk.epicguru.entity.Entity;
import co.uk.epicguru.main.FOE;

public class EntityBody extends Component {

	public Vector2 offset = new Vector2();
	
	private Body body;
	
	public EntityBody(Body body){
		this.setBody(body);
	}
	
	public void destroyBody(){
		this.destroyBody(null);
	}
	
	public void destroyBody(Runnable uponDestroyed){
		FOE.engine.removeBody(body, uponDestroyed);
		body = null;
	}
	
	public Body getBody(){
		return this.body;
	}
	
	public Body setBody(Body body){
		this.body = body;
		if(body != null)
			body.setUserData(this); // TEST TODO MAKE SOMETHING USEFUL LIKE COLLISION.
		return body;
	}
	
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
	}
	
	public String toString(){
		return "Physics Body";
	}
	
}
