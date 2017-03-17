package co.uk.epicguru.screens.hooks;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import co.uk.epicguru.API.screens.ScreenHook;
import co.uk.epicguru.main.Main;

public class PlayerRenderer extends ScreenHook{

	public TextureRegion[] walk;
	public TextureRegion[] damage;
	public TextureRegion[] headshot;
	
	public PlayerRenderer(){		
		TextureRegion region = Main.INSTANCE.getAsset("Textures/Player/Player.png", TextureRegion.class);
	}
	
	@Override
	public void update(float delta) {

	}

	@Override
	public void render(float delta, Batch batch) {
		
	}

	@Override
	public void renderUI(float delta, Batch batch) {

	}

}
