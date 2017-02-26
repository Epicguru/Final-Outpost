package co.uk.epicguru.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import co.uk.epicguru.API.screens.GameScreen;
import co.uk.epicguru.main.Main;

public final class MainMenu extends GameScreen {

	public Texture texture;
	
	public MainMenu(){
		this.texture = Main.INSTANCE.getAssetLoader().get("Background.png", Texture.class);
	}
	
	public void show(){
		
	}
	
	public void hide(){
		
	}
	
	public void update(float delta){
		
	}
	
	public void render(float delta, Batch batch){
		float hw = getScreenWidth() / 2f;
		float hh = getScreenHeight() / 2f;
		super.renderToSize(batch, texture, hw, hh, getScreenWidth(), getScreenHeight());
	}
	
}
