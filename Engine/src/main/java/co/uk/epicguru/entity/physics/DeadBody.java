package co.uk.epicguru.entity.physics;

import com.badlogic.gdx.physics.box2d.Body;

public class DeadBody {

	public Body body;
	public Runnable run;
	
	public DeadBody(Body body, Runnable run){
		this.body = body;
		this.run = run;
	}
	
}
