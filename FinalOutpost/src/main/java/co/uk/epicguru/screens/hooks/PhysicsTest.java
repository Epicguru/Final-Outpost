package co.uk.epicguru.screens.hooks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.Batch;

import co.uk.epicguru.API.screens.ScreenHook;
import co.uk.epicguru.input.Input;
import co.uk.epicguru.main.Constants;
import co.uk.epicguru.main.FOE;
import co.uk.epicguru.physics.ForceMode;
import co.uk.epicguru.physics.JPhysics;
import co.uk.epicguru.physics.JPhysicsBody;

public class PhysicsTest extends ScreenHook {

	private JPhysicsBody a, b;
	
	public void show(){
		
		JPhysics.reset();
		JPhysics.setPPM(Constants.PPM);
		JPhysics.setGravity(JPhysics.GRAVITY_ZERO);
		
		a = new JPhysicsBody(5, 10, 1, 1);
		b = new JPhysicsBody(5, 5, 3, 3);
	}
	
	public void update(float delta){
		
		JPhysics.update(delta);
		
		if(Input.getMouseButtonJustDown(Buttons.MIDDLE)){
			b.setPosition(Input.getMouseWorldPos());
			b.applyForce(Gdx.input.getDeltaX() * 5, -Gdx.input.getDeltaY() * 5, ForceMode.IMPULSE);
			
		}
	}
	
	public void render(float delta, Batch batch){
		
		batch.end();
		
		a.render(FOE.camera);
		b.render(FOE.camera);
		
		batch.begin();
	}
	
}
