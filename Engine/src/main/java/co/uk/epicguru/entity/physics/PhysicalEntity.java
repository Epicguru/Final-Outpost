package co.uk.epicguru.entity.physics;

import com.badlogic.gdx.physics.box2d.Body;

import co.uk.epicguru.entity.Entity;
import co.uk.epicguru.entity.components.Position;
import co.uk.epicguru.main.FOE;

public class PhysicalEntity extends Entity{

	private Body body;
	
	public PhysicalEntity(String name) {
		super(name);
		
		super.addComponents(new Position());		
	}	
	
	/**
	 * Updates the position component of this PhysicalEntity.
	 * <li>If you override this, YOU MUST CALL <code>super.update(float)</code>.
	 */
	public void update(float delta){
		Position pos = this.getComponent(Position.class);
		if(pos == null)
			return;
		else{
			if(this.hasBody()){
				pos.setBody(this.getBody());
				pos.setToBody();				
			}
		}
	}
	
	public boolean hasBody(){
		return this.getBody() != null;
	}
	
	public Body getBody(){
		return this.body;
	}
	
	public void setBody(Body body){
		this.body = body;
	}
	
	public void bodyRemoved(){
		// Callback
		print("Removed body!");
	}
	
	public void removeBody(){
		this.getComponent(Position.class).setBody(null);
		FOE.engine.removeBody(this.getBody(), () -> { this.bodyRemoved(); });
		this.body = null;
	}

	/**
	 * Call <code>super.removed()</code> if you override!!!
	 */
	public void removed() {
		this.removeBody();
	}
	
	
}
