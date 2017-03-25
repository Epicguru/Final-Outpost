package co.uk.epicguru.screens.hooks;

import com.badlogic.gdx.graphics.g2d.Batch;

import co.uk.epicguru.API.screens.ScreenHook;
import co.uk.epicguru.main.Constants;
import co.uk.epicguru.main.FOE;
import co.uk.epicguru.main.Main;
import co.uk.epicguru.physics.ForceMode;
import co.uk.epicguru.physics.JPhysics;
import co.uk.epicguru.physics.JPhysicsBody;

public class PhysicsTest extends ScreenHook {

	private JPhysicsBody a, b;
	
	public void show(){
		
		JPhysics.reset();
		JPhysics.setPPM(Constants.PPM);
		JPhysics.setGravity(JPhysics.GRAVITY_ZERO);
		
		a = new JPhysicsBody(5, 10, 1, 1).setDrag(0.98f, 0.98f);
		b = new JPhysicsBody(10, 30, 3, 3).setDrag(0.98f, 0.98f);
	}
	
	public void update(float delta){
		
		JPhysics.update(delta);
		
		if(Main.INSTANCE.isInputJustDown(Main.DEBUG)){
			a.setPosition(0, 0);
			a.applyForce(20, 0, ForceMode.IMPULSE);
		}
	}
	
	public void render(float delta, Batch batch){
		
		batch.end();
		
		a.render(FOE.camera);
		b.render(FOE.camera);
		
		batch.begin();
	}
	
}
