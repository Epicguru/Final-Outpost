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
import co.uk.epicguru.API.AllocatedTimer;
import co.uk.epicguru.API.Allocator;
import co.uk.epicguru.entity.components.ArmouredHealth;
import co.uk.epicguru.entity.components.Position;
import co.uk.epicguru.entity.physics.PhysicalEntity;
import co.uk.epicguru.input.Input;
import co.uk.epicguru.main.FOE;
import co.uk.epicguru.main.Main;

public class PlayerEntity extends PhysicalEntity implements Runnable{

	public PlayerRenderer renderer;
	private AllocatedTimer timer;
	private PointLight flashlight;
	
	public PlayerEntity() {
		super("Player");		
		super.addComponents(new Position(), new ArmouredHealth(100f, 100f, 0f));
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
	
	public void update(float delta){
		super.update(delta);
		
		if(Input.isKeyJustDown(Keys.L)){
			super.getComponent(ArmouredHealth.class).setHealth(0);
		}
		
		if(super.getComponent(ArmouredHealth.class).isDead()){
			dead();
		}
		
		this.flashlight.setColor(0, 0, 0, 1f);
		this.flashlight.setDistance(30);
		this.flashlight.attachToBody(null);
		this.flashlight.setPosition(Input.getMouseWorldPos());
	}
	
	public void dead(){
		FOE.engine.remove(this);
		print("Player has died!");
	}
	
	public void run(){
		
		// Runs 120 times per second, independently in another thread
		// Need to check body, because of threading
		if(super.getBody() == null)
			return; // TODO could still be error...

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
		
		super.getBody().setLinearVelocity(vel.x, vel.y);
	}
	
	public void added(){
		// Set body
		super.setBody(this.makeBody());
		super.getComponent(Position.class).setBody(super.getBody());
		
		// Set timer
		this.timer = AllocatedTimer.inSecond(120f, this);
		
		// Set renderer
		this.renderer = new PlayerRenderer();
		
		// Allocate timer
		Allocator.add(this.timer);
		
		// Create light
		this.flashlight = new PointLight(FOE.engine.getRayHandler(), FOE.engine.getRaysPerLight(), Color.SCARLET, 10, 0, 0);
	}
	
	public void removed(){
		super.removed();
		Allocator.removeTimer(this.timer);
		this.flashlight.remove();
	}
	
	public void render(Batch batch, float delta){	
		renderer.setPosition(FOE.camera.position.x, FOE.camera.position.y);
		renderer.update(delta);
		renderer.render(delta, batch);
	}
}
