package co.uk.epicguru.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import box2dLight.PointLight;
import co.uk.epicguru.IO.NotSerialized;
import co.uk.epicguru.entity.Entity;
import co.uk.epicguru.entity.components.Health;
import co.uk.epicguru.main.Constants;
import co.uk.epicguru.main.FOE;
import co.uk.epicguru.main.Main;

public class Bonfire extends Entity {

	private static int loadCycle;
	private static TextureRegion texture;
	
	public boolean burning = true;
	public Color lightColour = new Color(0.1f, 0, 0.05f, 1f);
	
	@NotSerialized private PointLight light;
	@NotSerialized private float timer, timer2;
	@NotSerialized private final float particlesPerSecond = 120f;
	
	public Bonfire() {
		super("Bonfire");
	}
	
	public void added(boolean loaded){
		
		if(loaded){
			// Nothing to do, done for us.
		}else{			
			super.addComponents(new Health(20f, 20f));			
		}	
		
		light = new PointLight(FOE.engine.getRayHandler(), FOE.engine.getRaysPerLight(), lightColour, 25f, getX(), getY());
		
		if(loadCycle != FOE.loadCycle){
			texture = Main.INSTANCE.getAsset("Textures/Entities/Bonfire.png", TextureRegion.class);
			
			loadCycle = FOE.loadCycle;
		}
	}
	
	public void die(){
		FOE.engine.remove(this);
	}
	
	public void spawnParticles(float delta){
		
		timer2 += delta;
		float interval = 1f / this.particlesPerSecond;
		
		while(timer2 >= interval){
			
			FireParticle fire = new FireParticle();
			
			float width = texture.getRegionWidth() / Constants.PPM;
			
			fire.setPosition(this.getX() + width * MathUtils.random() - 0.25f, this.getY() + MathUtils.random(0.25f, 0.9f));
			
			FOE.engine.add(fire);
			
			timer2 -= interval;
			
		}
		
	}
	
	public void update(float delta){
		
		// Check if dead
		if(super.getComponent(Health.class).isDead()){
			die();
		}
		
		timer += delta;
		
		// Set light position
		light.setPosition(this.getX() + texture.getRegionWidth() / Constants.PPM / 2f, this.getY()  + texture.getRegionHeight() / Constants.PPM / 2f);		
		
		// Set light 'intensity'
		float intensity = 0.9f + MathUtils.cosDeg(timer * 200f) * 0.035f;	
		this.lightColour.a = intensity;
		
		// Apply light colour
		this.light.setColor(this.lightColour);
		
		// Spawn particles if burning
		if(burning)
			spawnParticles(delta);
		
		// Activate light if burning
		light.setActive(burning);
	}
	
	public void removed(){
		this.light.remove();
	}
	
	public void render(Batch batch, float delta){
		
		batch.draw(texture, getX(), getY(), texture.getRegionWidth() / Constants.PPM, texture.getRegionHeight() / Constants.PPM);
		
	}
}
