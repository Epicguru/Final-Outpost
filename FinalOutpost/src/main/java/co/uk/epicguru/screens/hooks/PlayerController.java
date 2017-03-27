package co.uk.epicguru.screens.hooks;

import com.badlogic.gdx.graphics.g2d.Batch;

import co.uk.epicguru.API.AllocatedTimer;
import co.uk.epicguru.API.Allocator;
import co.uk.epicguru.API.screens.ScreenHook;
import co.uk.epicguru.main.Constants;
import co.uk.epicguru.main.Main;
import co.uk.epicguru.physics.ForceMode;
import co.uk.epicguru.physics.JPhysicsBody;
import co.uk.epicguru.player.PlayerRenderer;

public class PlayerController extends ScreenHook implements Runnable{
	// TODO with entity classes
	
	public PlayerRenderer renderer;
	public JPhysicsBody body;
	private AllocatedTimer timer;
	
	public void show(){
		renderer = new PlayerRenderer();
		body = new JPhysicsBody(0, 0, 16f / Constants.PPM * 2, 32f / Constants.PPM * 2).setDrag(0.7f, 0.7f);
		
		timer = Allocator.add(AllocatedTimer.inSecond(120f, this)); // UPS for input
	}
	
	public void hide(){
		Allocator.removeTimer(timer);
		timer = null;
		body = null;
		renderer = null;
	}
	
	public void updateInput(){
		
		// Input
		float speed = 2.2f;
		//boolean p = false;
		
		if(Main.INSTANCE.isInputDown(Main.LEFT)){
			body.applyForce(-speed, 0, ForceMode.FORCE);
			//p = true;
		}
		if(Main.INSTANCE.isInputDown(Main.RIGHT)){
			body.applyForce(speed, 0, ForceMode.FORCE);
			//p = true;
		}
		if(Main.INSTANCE.isInputDown(Main.DOWN)){
			body.applyForce(0, -speed, ForceMode.FORCE);
			//p = true;
		}
		if(Main.INSTANCE.isInputDown(Main.UP)){
			body.applyForce(0, speed, ForceMode.FORCE);
			//p = true;
		}
	}
	
	public void update(float delta){
		
		// Position
		renderer.update(delta);
		renderer.setPosition(body.getX(), body.getY());
	}
	
	public void render(float delta, Batch batch){
		renderer.render(delta, batch);
	}
	
	public void run(){
		updateInput();
	}	
}
