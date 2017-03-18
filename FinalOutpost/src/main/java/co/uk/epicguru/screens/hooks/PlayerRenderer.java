package co.uk.epicguru.screens.hooks;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import co.uk.epicguru.API.screens.ScreenHook;
import co.uk.epicguru.input.Input;
import co.uk.epicguru.main.Constants;
import co.uk.epicguru.main.Main;

public class PlayerRenderer extends ScreenHook{

	public TextureRegion[] walk;
	public TextureRegion[] damage;
	public TextureRegion[] headshot;
	
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

	}

	@Override
	public void render(float delta, Batch batch) {
		drawPlayer(batch);
	}
	
	public void drawPlayer(Batch batch){
		int frame = getFrame();
		float scale = getScale();
		batch.draw(walk[frame], Input.getMouseWorldX(), Input.getMouseWorldY(), walk[frame].getRegionWidth() / Constants.PPM * scale, walk[frame].getRegionHeight() / Constants.PPM * scale);
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
