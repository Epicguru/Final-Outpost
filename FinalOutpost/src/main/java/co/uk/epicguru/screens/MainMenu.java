package co.uk.epicguru.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import co.uk.epicguru.API.screens.GameScreen;
import co.uk.epicguru.input.Input;
import co.uk.epicguru.main.Main;
import ro.fortsoft.pf4j.Extension;

@Extension
public final class MainMenu extends GameScreen {

	public TextureRegion background;
	
	public MainMenu(){
		this.background = Main.INSTANCE.getAsset("Textures/UI/TitleBackground.png", TextureRegion.class);
	}
	
	public void update(float delta){
		
		// Hooks
		super.update(delta);
	}
	
	public void render(float delta, Batch batch){
		
		// Hooks
		super.render(delta, batch);
	}
	
	public void renderUI(float delta, Batch batch){	
		batch.setColor(Color.WHITE);
		
		// Background
		float hw = getScreenWidth() / 2f;
		float hh = getScreenHeight() / 2f;
		
		float m = 1.4f;
		float addX = Input.getMouseX() - hw;
		float addY = getScreenHeight() - Input.getMouseY() - hh;
		
		addX *= 0.1f;
		addY *= 0.1f;
		
		// Background
		super.renderToSize(batch, this.background, hw + addX, hh + addY, getScreenWidth() * m, getScreenHeight() * m);		
		
		// Hooks
		super.renderUI(delta, batch);
	}
	
}
