package co.uk.epicguru.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import box2dLight.PointLight;
import co.uk.epicguru.API.time.GameTime;
import co.uk.epicguru.IO.NotSerialized;
import co.uk.epicguru.entity.Entity;
import co.uk.epicguru.entity.components.Health;
import co.uk.epicguru.main.Constants;
import co.uk.epicguru.main.FOE;
import co.uk.epicguru.main.Main;

public class Bonfire extends Entity {

	private static TextureRegion texture;
	public boolean burning = true;
	public Color lightColour = new Color(0.1f, 0, 0.05f, 1f);
	
	@NotSerialized private PointLight light;
	@NotSerialized private float timer;
	
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
		
		if(texture == null){
			texture = Main.INSTANCE.getAsset("Textures/Entities/Bonfire.png", TextureRegion.class);
		}
	}
	
	public void die(){
		FOE.engine.remove(this);
	}
	
	public void update(float delta){
		
		// Check if dead
		if(super.getComponent(Health.class).isDead()){
			die();
		}
		
		timer += delta;
		
		GameTime.add(-GameTime.getTime());
		
		// Set light position
		light.setPosition(this.getX() + texture.getRegionWidth() / Constants.PPM / 2f, this.getY()  + texture.getRegionHeight() / Constants.PPM / 2f);		
		
		// Set light 'intensity'
		float intensity = MathUtils.clamp(0.9f + MathUtils.sinDeg(timer * 200f) * 0.05f, 0.5f, 1);		
		this.lightColour.a = intensity;
		
		// Apply light colour
		this.light.setColor(this.lightColour);
	}
	
	public void removed(){
		this.light.remove();
	}
	
	public void render(Batch batch, float delta){
		
		batch.draw(texture, getX(), getY(), texture.getRegionWidth() / Constants.PPM, texture.getRegionHeight() / Constants.PPM);
		
	}
}
