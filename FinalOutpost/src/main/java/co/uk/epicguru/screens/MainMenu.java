package co.uk.epicguru.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import co.uk.epicguru.API.screens.GameScreen;
import co.uk.epicguru.input.Input;
import co.uk.epicguru.main.Main;
import ro.fortsoft.pf4j.Extension;

@Extension
public final class MainMenu extends GameScreen {

	public TextureRegion texture;
	public BitmapFont font;
	
	public MainMenu(){
		this.texture = Main.INSTANCE.getAsset("Textures/UI/TitleBackground.png", TextureRegion.class);
		this.font = Main.INSTANCE.getAsset("Fonts/Title.fnt", BitmapFont.class);
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
		
		super.renderToSize(batch, texture, hw + addX, hh + addY, getScreenWidth() * m, getScreenHeight() * m);
		
		// Title
		font.draw(batch, "Final Outpost", getScreenWidth() / 2f, getScreenHeight() - 10, 0, 1, false);
		
		
		// Hooks
		super.renderUI(delta, batch);
	}
	
}
