package co.uk.epicguru.entity.physics;

import com.badlogic.gdx.physics.box2d.Body;

import co.uk.epicguru.entity.Entity;
import co.uk.epicguru.entity.components.Position;

public class PhysicalEntity extends Entity{

	private Body body;
	
	public PhysicalEntity(String name) {
		super(name);
		
		super.addComponents(new Position());		
	}	
	
	public void update(float delta){
		Position pos = this.getComponent(Position.class);
		if(pos == null)
			return;
		
		if(pos.isBody())
			pos.setToBody();
		else
			pos.setBody(this.getBody());
	}
	
	public Body getBody(){
		return this.body;
	}
	
	public void removeBody(){
		this.body = null;
		// TODO remove body
	}
}
