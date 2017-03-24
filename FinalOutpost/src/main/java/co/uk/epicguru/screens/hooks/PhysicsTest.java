package co.uk.epicguru.screens.hooks;

import com.badlogic.gdx.graphics.g2d.Batch;

import co.uk.epicguru.API.screens.ScreenHook;
import co.uk.epicguru.main.Constants;
import co.uk.epicguru.main.FOE;
import co.uk.epicguru.main.Main;
import co.uk.epicguru.physics.JPhysics;
import co.uk.epicguru.physics.JPhysicsBody;

public class PhysicsTest extends ScreenHook {

	private JPhysicsBody a, b;
	
	public void show(){
		
		JPhysics.reset();
		JPhysics.setPPM(Constants.PPM);
		
		a = new JPhysicsBody(5, 10, 1, 1);
		b = new JPhysicsBody(10, 30, 3, 3);
	}
	
	public void update(float delta){
		a.update(delta);
		b.update(delta);
		JPhysics.setGravity(JPhysics.GRAVITY_ZERO);
		if(Main.INSTANCE.isInputJustDown(Main.DEBUG)){
			a = new JPhysicsBody(0, 0, 1, 1);
			b = new JPhysicsBody(10, 30, 3, 3);
		}
	}
	
	public void render(float delta, Batch batch){
		
		batch.end();
		
		a.render(FOE.camera);
		b.render(FOE.camera);
		
		batch.begin();
	}
	
}
