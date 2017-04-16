package co.uk.epicguru.physics;

import com.badlogic.gdx.physics.box2d.Body;

import co.uk.epicguru.entity.Entity;

public class BodyUtils {

	public static Entity getEntity(Body body){
		if(body.getUserData() == null)
			return null;
		
		if(body.getUserData() instanceof Entity){
			return (Entity)body.getUserData();
		}else{
			return null;
		}
	}
	
}
