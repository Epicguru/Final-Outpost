package co.uk.epicguru.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import box2dLight.PointLight;
import co.uk.epicguru.entity.Entity;
import co.uk.epicguru.entity.components.ArmouredHealth;
import co.uk.epicguru.entity.physics.EntityBody;
import co.uk.epicguru.input.Input;
import co.uk.epicguru.main.FOE;
import co.uk.epicguru.main.Main;

public class PlayerEntity extends Entity {

	public PlayerRenderer renderer;
	
	private ArmouredHealth health;
	private EntityBody body;
	private PointLight flashlight;
	private float timer;
	
	public PlayerEntity() {
		super("Player");		
		super.addComponents(health = new ArmouredHealth(100f, 100f, 0f));
	}
	
	private Body makeBody(){
		BodyDef def = FOE.engine.newBodyDef();
		
		def.bullet = true;
		def.allowSleep = false;
		def.type = BodyType.DynamicBody;
		
		PolygonShape shape = FOE.engine.boxOfSize(0.95f, 1.95f);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		
		Body b = FOE.engine.newBody(this, def);
		b.createFixture(fixture);
		shape.dispose();
		
		return b;
	}
	
	public void updateLightPos(){
		this.flashlight.setColor(0, 0, 0, 1f);
		this.flashlight.setDistance(10);
		this.flashlight.setPosition(Input.getMouseWorldPos());
	}
	
	public void checkForDeath(){
		if(health.isDead()){
			dead();
		}
	}
	
	public void updateMovement(float delta){
		timer += delta;
		float delay = 1f / 120f;
		while(timer >= delay){
			timer -= delay;
			this.doMovement();
		}
	}
	
	public void doMovement(){
		if(body.getBody() == null)
			return;

		// Speed (constant for now)
		float speed = 4f;		
		
		// This constantly resets speed - Think of a better way to do this!
		Vector2 vel = new Vector2();
		
		// Move using user-defined keys.		
		if(Main.INSTANCE.isInputDown(Main.RIGHT)){
			vel.x += speed;
		}
		if(Main.INSTANCE.isInputDown(Main.LEFT)){
			vel.x -= speed;
		}
		if(Main.INSTANCE.isInputDown(Main.UP)){
			vel.y += speed;
		}
		if(Main.INSTANCE.isInputDown(Main.DOWN)){
			vel.y -= speed;
		}
		
		body.getBody().setLinearVelocity(vel.x, vel.y);
	}
	
	public void dead(){
		FOE.engine.remove(this);
		print("Player has died!");
	}
	
	public void update(float delta){
		
		// TEST!
		if(Input.isKeyJustDown(Keys.L)){
			super.getComponent(ArmouredHealth.class).setHealth(0);
		}
		
		// Update position
		body.update(this);
		
		// Ensure that we are not dead!
		checkForDeath();
		
		// Light
		updateLightPos();
		
		// Movement on timer.
		updateMovement(delta);		
		
		// Check for death again, in case we were killed by movement.
		checkForDeath();
	}
	
	public void added(){
		// Set body
		super.addComponents(body = new EntityBody(makeBody()));
		
		// Set renderer
		this.renderer = new PlayerRenderer();
		
		// Create light
		this.flashlight = new PointLight(FOE.engine.getRayHandler(), FOE.engine.getRaysPerLight(), Color.SCARLET, 10, 0, 0);
	}
	
	public void removed(){
		this.flashlight.remove();
		this.body.destroyBody();
		print("Player removed.");
	}
	
	public void render(Batch batch, float delta){	
		renderer.setPosition(FOE.camera.position.x, FOE.camera.position.y);
		renderer.update(delta);
		renderer.render(delta, batch);
	}
}
