package co.uk.epicguru.player;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import co.uk.epicguru.main.Constants;
import co.uk.epicguru.main.Main;

public class PlayerRenderer {

	private static TextureRegion[] walkFrames;
	private Vector2 position = new Vector2();
	private float timer;
	private int frame;
	
	public PlayerRenderer(){
		if(walkFrames == null){
			walkFrames = new TextureRegion[2];
			
			walkFrames[0] = Main.INSTANCE.getAsset("Textures/Player/Walk0.png", TextureRegion.class);
			walkFrames[1] = Main.INSTANCE.getAsset("Textures/Player/Walk1.png", TextureRegion.class);
		}
	}
	
	public void setPosition(Vector2 pos){
		this.position.set(pos);
	}
	
	public void setPosition(float x, float y){
		this.position.set(x, y);		
	}
	
	public void update(float delta){
		
		timer += delta;
		float FPS = 10f;
		float interval = 1f / FPS;
		while(timer >= interval){
			timer -= interval;
			
			frame += 1;
			frame %= walkFrames.length;
		}
	}
	
	public void render(float deltaTime, Batch batch){
		TextureRegion t = walkFrames[frame];
		batch.draw(t, position.x, position.y, t.getRegionWidth() / Constants.PPM * 2f, t.getRegionHeight() / Constants.PPM * 2f);
	}

	public void resetFrame() {
		this.frame = 0;		
	}
	
}
