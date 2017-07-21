package co.uk.epicguru.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import co.uk.epicguru.API.U;
import co.uk.epicguru.entity.Entity;
import co.uk.epicguru.main.Constants;
import co.uk.epicguru.main.FOE;
import co.uk.epicguru.main.Main;

public class FireParticle extends Entity {

	private static int loadCycle;
	private static TextureRegion texture;
	
	private Vector2 velocity;
	private float timeRemaining, topTime;
	private float p;
	
	public FireParticle() {
		super("Fire Particle");
		super.setSerializes(false);
	}
	
	public void added(boolean loaded){
		
		if(!loaded){
			velocity = new Vector2(MathUtils.random(-0.2f, 0.2f), MathUtils.random(1, 2.5f));
			topTime = MathUtils.random(0.5f, 1);
			timeRemaining = topTime;
			p = MathUtils.random();
		}
		
		if(loadCycle != FOE.loadCycle){
			texture = Main.INSTANCE.getAsset("Textures/Entities/Fire Particle.png", TextureRegion.class);
			
			loadCycle = FOE.loadCycle;
		}
		
	}
	
	public void update(float delta){
		
		super.offset(velocity, delta);
		
		timeRemaining -= delta;
		if(timeRemaining <= 0){
			FOE.engine.remove(this);
		}
		
	}
	
	public void render(Batch batch, float delta){
		
		float width = texture.getRegionWidth() / Constants.PPM;
		float height = texture.getRegionHeight() / Constants.PPM;
		
		Color c = U.lerp(Color.YELLOW, Color.RED, p);
		c.a = timeRemaining / topTime;
		batch.setColor(c);
		batch.draw(texture, getX(), getY(), width, height);
		batch.setColor(Color.WHITE);
		
	}
	
}
