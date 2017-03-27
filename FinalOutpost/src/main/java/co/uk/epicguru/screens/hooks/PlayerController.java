package co.uk.epicguru.screens.hooks;

import com.badlogic.gdx.graphics.g2d.Batch;

import co.uk.epicguru.API.screens.ScreenHook;
import co.uk.epicguru.main.Constants;
import co.uk.epicguru.main.FOE;
import co.uk.epicguru.main.Main;
import co.uk.epicguru.physics.ForceMode;
import co.uk.epicguru.physics.JPhysicsBody;
import co.uk.epicguru.player.PlayerRenderer;

public class PlayerController extends ScreenHook{
	// TODO with entity classes
	
	public PlayerRenderer renderer;
	private JPhysicsBody body;
	private float timer;
	
	public void show(){
		renderer = new PlayerRenderer();
		body = new JPhysicsBody(0, 0, 16f / Constants.PPM * 2, 32f / Constants.PPM * 2);
	}
	
	public void updateInput(float delta){
		
		float speed = 2.2f;
		boolean p = false;
		
		body.setDrag(0.7f, 0.7f);
		
		// Renderer
		renderer.update(delta);
		
		timer += delta;
		float interval = 1f / 120f;
		
		while(timer >= interval){
			timer -= interval;
			
			// Input - 60 times per second, since it depends on frame rate.
			if(Main.INSTANCE.isInputDown(Main.LEFT)){
				body.applyForce(-speed, 0, ForceMode.FORCE);
				p = true;
			}
			if(Main.INSTANCE.isInputDown(Main.RIGHT)){
				body.applyForce(speed, 0, ForceMode.FORCE);
				p = true;
			}
			if(Main.INSTANCE.isInputDown(Main.DOWN)){
				body.applyForce(0, -speed, ForceMode.FORCE);
				p = true;
			}
			if(Main.INSTANCE.isInputDown(Main.UP)){
				body.applyForce(0, speed, ForceMode.FORCE);
				p = true;
			}
			
			if(!p){
				renderer.resetFrame();
			}
			
		}
	}
	
	public void update(float delta){
		
		// Input
		updateInput(delta);	
		
		// Position
		renderer.setPosition(body.getX(), body.getY());
		
		// Camera
		FOE.camera.position.set(body.getX(), body.getY(), 0);
	}
	
	public void render(float delta, Batch batch){
		renderer.render(delta, batch);
	}
	
}
