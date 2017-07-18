package co.uk.epicguru.player;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import co.uk.epicguru.entity.Entity;
import co.uk.epicguru.main.Constants;
import co.uk.epicguru.main.FOE;
import co.uk.epicguru.main.Main;

public class TestEntity extends Entity {

	private static TextureRegion texture;
	
	public Vector2 velocity;
	public float timeRemaining;
	
	public TestEntity() {
		super("TestEntity");		
	}
	
	public void update(float delta){
		
		// Move
		this.offset(this.velocity, delta);
		
		// Check to see if we are dead.
		this.timeRemaining -= delta;
		if(timeRemaining <= 0){
			FOE.engine.remove(this);
		}		
	}
	
	public void added(boolean loaded){
		
		if(texture == null){
			texture = Main.INSTANCE.getAsset("Textures/Player/TestEntity.png", TextureRegion.class);
		}
		
		if(loaded) // Stop here if we have loaded, all these values will be set for us.
			return;
		
		timeRemaining = MathUtils.random(1, 5);
		
		this.velocity = new Vector2(MathUtils.random(-5, 5), MathUtils.random(-5, 5));
		
	}
	
	public void render(Batch batch, float delta) {
		
		batch.draw(texture, this.getX(), this.getY(), texture.getRegionWidth() / Constants.PPM, texture.getRegionHeight() / Constants.PPM);
		
	}	
}
