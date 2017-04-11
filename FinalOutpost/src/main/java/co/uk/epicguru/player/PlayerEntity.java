package co.uk.epicguru.player;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;

import co.uk.epicguru.entity.Entity;
import co.uk.epicguru.entity.Group;
import co.uk.epicguru.entity.components.ArmouredHealth;
import co.uk.epicguru.entity.components.Position;
import co.uk.epicguru.input.Input;
import co.uk.epicguru.main.FOE;
import co.uk.epicguru.main.Main;

public class PlayerEntity extends Entity {

	public PlayerRenderer renderer;
	
	public PlayerEntity() {
		super("Player");		
		super.addComponents(new Position(), new ArmouredHealth(100f, 100f, 0f));	
		
		this.renderer = new PlayerRenderer();
	}
	
	public void update(float delta){
		// TODO make with physics
		
		Position pos = super.getComponent(Position.class);
		float speed = 4f;
		if(Main.INSTANCE.isInputDown(Main.RIGHT)){
			pos.addX(delta * speed);
		}
		if(Main.INSTANCE.isInputDown(Main.LEFT)){
			pos.addX(delta * -speed);
		}
		if(Main.INSTANCE.isInputDown(Main.UP)){
			pos.addY(delta * speed);
		}
		if(Main.INSTANCE.isInputDown(Main.DOWN)){
			pos.addY(delta * -speed);
		}
		
		if(Input.isKeyJustDown(Keys.G)){
			ArrayList<Entity> list = FOE.engine.ofGroup(Group.of(Position.class));
			
			print("Found : ");
			for(Entity e : list){
				print(e.toString() + ":");
				System.out.println(e.getComponentDetails());
			}
		}		
	}
	
	public void render(Batch batch, float delta){	
		renderer.setPosition(FOE.camera.position.x, FOE.camera.position.y);
		renderer.update(delta);
		renderer.render(delta, batch);
	}
}
