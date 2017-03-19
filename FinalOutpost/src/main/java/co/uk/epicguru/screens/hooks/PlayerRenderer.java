package co.uk.epicguru.screens.hooks;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import co.uk.epicguru.API.screens.ScreenHook;
import co.uk.epicguru.main.Constants;
import co.uk.epicguru.main.FOE;
import co.uk.epicguru.main.Main;

public class PlayerRenderer extends ScreenHook{

	public TextureRegion[] walk;
	public TextureRegion[] damage;
	public TextureRegion[] headshot;
	public Vector2 position = new Vector2();
	
	public PlayerRenderer(){		
		TextureRegion w0 = Main.INSTANCE.getAsset("Textures/Player/Walk0.png", TextureRegion.class);
		TextureRegion w1 = Main.INSTANCE.getAsset("Textures/Player/Walk1.png", TextureRegion.class);
		
		walk = new TextureRegion[] {
				w0,
				w1
			};
	}
	
	@Override
	public void update(float delta) {
		
		float speed = 10;
		speed *= delta;
		
		if(Main.INSTANCE.isInputDown(Main.RIGHT)){
			position.x += speed;
		}
		if(Main.INSTANCE.isInputDown(Main.LEFT)){
			position.x -= speed;
		}
		if(Main.INSTANCE.isInputDown(Main.UP)){
			position.y += speed;
		}
		if(Main.INSTANCE.isInputDown(Main.DOWN)){
			position.y -= speed;
		}
		
		FOE.camera.position.set(this.position, 0);
	}

	@Override
	public void render(float delta, Batch batch) {
		drawPlayer(batch);
	}
	
	public void drawPlayer(Batch batch){
		int frame = getFrame();
		float scale = getScale();
		batch.draw(walk[frame], getPosition().x, getPosition().y, walk[frame].getRegionWidth() / Constants.PPM * scale, walk[frame].getRegionHeight() / Constants.PPM * scale);
	}
	
	public Vector2 getPosition(){
		return position;
	}
	
	public int getFrame(){
		return 0;
	}
	
	public float getScale(){
		return 2f;
	}
	
	public TextureRegion[] getFrames(){
		return walk;
	}

	@Override
	public void renderUI(float delta, Batch batch) {

	}

}
