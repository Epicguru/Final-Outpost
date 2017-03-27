package co.uk.epicguru.screens.hooks;

import com.badlogic.gdx.graphics.g2d.Batch;

import co.uk.epicguru.entities.EntityBody;
import co.uk.epicguru.main.Constants;
import co.uk.epicguru.main.Main;
import co.uk.epicguru.physics.ForceMode;
import co.uk.epicguru.player.PlayerRenderer;

public class PlayerController extends EntityBody{
	// TODO with entity classes
	
	public PlayerRenderer renderer;
	
	public PlayerController(){
		super("Player");
		renderer = new PlayerRenderer();		
		super.startInput(120);
		
		// Body
		super.setSize(16f / Constants.PPM * 2, 32f / Constants.PPM * 2);
		super.getBody().setDrag(0.7f, 0.7f);
	}
	
	public void input(){
		
		// Input
		float speed = 2.2f;
		//boolean p = false;
		
		if(Main.INSTANCE.isInputDown(Main.LEFT)){
			getBody().applyForce(-speed, 0, ForceMode.FORCE);
			//p = true;
		}
		if(Main.INSTANCE.isInputDown(Main.RIGHT)){
			getBody().applyForce(speed, 0, ForceMode.FORCE);
			//p = true;
		}
		if(Main.INSTANCE.isInputDown(Main.DOWN)){
			getBody().applyForce(0, -speed, ForceMode.FORCE);
			//p = true;
		}
		if(Main.INSTANCE.isInputDown(Main.UP)){
			getBody().applyForce(0, speed, ForceMode.FORCE);
			//p = true;
		}
	}
	
	public void update(float delta){
		
		// Position
		renderer.update(delta);
		renderer.setPosition(getX(), getY());
	}
	
	public void render(float delta, Batch batch){
		renderer.render(delta, batch);
	}	
}
