package co.uk.epicguru.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import co.uk.epicguru.API.screens.GameScreen;
import co.uk.epicguru.UI.Image;
import co.uk.epicguru.UI.Observer;
import co.uk.epicguru.UI.loading.LoadingSymbol;
import co.uk.epicguru.input.Input;
import co.uk.epicguru.main.Main;
import ro.fortsoft.pf4j.Extension;

@Extension
public final class MainMenu extends GameScreen {

	public TextureRegion background;
	public TextureRegion title;
	public Observer obs;
	
	public Image titleImage;
	
	public void show(){
		
		// Reset UI
		obs = new Observer();
		
		// Load assets, may be again...
		this.background = Main.INSTANCE.getAsset("Textures/UI/TitleBackground.png", TextureRegion.class);
		this.title = Main.INSTANCE.getAsset("Textures/UI/Title.png", TextureRegion.class);
		
		// UI elements
		this.titleImage = new Image(this.title);
		
		// Hooks
		super.show();
	}
	
	public void hide(){
		
		this.obs = null;
		this.title = null;
		this.background = null;
		this.titleImage = null;
		this.titleImage = null;
		
		// Hooks
		super.hide();
	}
	
	public void update(float delta){
		
		// Made for a 1080p monitor : we need to find the right scale for other monitor resolutions. We will simply scale
		// up or down depending on the resolution aspect, using horizontal pixels.
		// (Will this affect ultra and super wide monitor users? Ooops.)
		
		// Get relative size
		float scale = Gdx.graphics.getWidth() / 1920f;
		obs.setScale(scale); // Something for 4k ?? :D
		
		// Title
		titleImage.bounds.setPosition(0, Gdx.graphics.getHeight() - titleImage.getFinalHeight(obs) + titleImage.getFinalHeight(obs) / 5f);
		
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
		float addY = getScreenHeight() + Input.getMouseY() - hh;
		
		addX *= 0.1f;
		addY *= 0.1f;
		
		// Background
		super.renderToSize(batch, this.background, hw + addX, hh + addY, getScreenWidth() * m, getScreenHeight() * m);		
		
		// Title
		titleImage.render(obs, delta);
		
		// Just cuz
		LoadingSymbol.render(0, 0, obs, delta / 3f);
		LoadingSymbol.render(Gdx.graphics.getWidth(), 0, obs, delta / 3f);
		LoadingSymbol.render(Input.getMouseX(), Input.getMouseY(), obs, delta / 3f);
		
		// Hooks
		super.renderUI(delta, batch);
	}
	
}
