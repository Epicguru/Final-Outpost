package co.uk.epicguru.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

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
	private float p = 0;
	private float direction = 1;
	private float timer = 0;
	
	public Image titleImage;
	
	public void show(){
		
		// Reset UI
		obs = new Observer();
		
		// Load assets, may be again...
		this.background = Main.INSTANCE.getAsset("Textures/UI/TitleBackground.png", TextureRegion.class);
		this.title = Main.INSTANCE.getAsset("Textures/UI/Title.png", TextureRegion.class);
		
		// UI elements
		this.titleImage = new Image(this.title);
		
		// Interpolation effects
		p = 0;
		
		// Misc
		timer = 0;
		
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
		
		// Timer
		timer += delta;
		
		// Made for a 1080p monitor : we need to find the right scale for other monitor resolutions. We will simply scale
		// up or down depending on the resolution aspect, using horizontal pixels.
		// (Will this affect ultra and super wide monitor users? Ooops.)
		
		// Get relative size
		float scale = Gdx.graphics.getWidth() / 1920f;
		obs.setScale(scale); // Something for 4k ?? :D
		
		// Interpolation effects percentage
		float seconds = 2; // Lasts 2 seconds
		p += (delta * direction) / seconds;
		if(p > 1)
			p = 1;
		if(p < 0)
			p = 0;
		
		if(Input.isKeyJustDown(Keys.Z)){
			this.direction = 1;
			this.p = 0;
		}
		if(Input.isKeyJustDown(Keys.X)){
			this.direction = -1;
			this.p = 1;
		}
		
		// Hooks
		super.update(delta);
	}
	
	public void render(float delta, Batch batch){
		
		// Hooks
		super.render(delta, batch);
	}
	
	public void cogs(float delta, Batch batch){
		Interpolation i = Interpolation.swing;
		
		float add = 200;
		
		LoadingSymbol.render(i.apply(-add, 0, this.p), 0, obs, delta / 2f);
		LoadingSymbol.render(i.apply(Gdx.graphics.getWidth() + add, Gdx.graphics.getWidth(), this.p), 0, obs, delta / 2f);
	}
	
	public void title(float delta, Batch batch){
		Interpolation i = Interpolation.pow4;
		
		// Position
		float x = i.apply(Gdx.graphics.getWidth(), 0, this.p);
		titleImage.bounds.setPosition(x,
				Gdx.graphics.getHeight() - titleImage.getFinalHeight(obs) + titleImage.getFinalHeight(obs) / 5f + MathUtils.cosDeg(timer * 80f) * 15f);
	
		titleImage.render(obs, delta);
	}
	
	public void renderUI(float delta, Batch batch){	
		batch.setColor(Color.WHITE);
		
		// Background
		float hw = getScreenWidth() / 2f;
		float hh = getScreenHeight() / 2f;
		
		float m = 1.4f;
		float addX = Input.getMouseX() - hw;
		float addY = getScreenHeight() + Input.getMouseY() - hh;
		
		addX *= 0.05f;
		addY *= 0.05f;
		
		// Background
		batch.setColor(new Color(p, p, p, 1));
		super.renderToSize(batch, this.background, hw + addX, hh + addY, getScreenWidth() * m, getScreenHeight() * m);	
		batch.setColor(Color.WHITE);
		
		// Title
		this.title(delta, batch);
		
		// Nice!
		this.cogs(delta, batch);
		
		// Hooks
		super.renderUI(delta, batch);
	}
	
}
