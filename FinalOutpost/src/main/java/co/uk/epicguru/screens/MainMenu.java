package co.uk.epicguru.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

import co.uk.epicguru.API.screens.GameScreen;
import co.uk.epicguru.UI.Image;
import co.uk.epicguru.UI.Observer;
import co.uk.epicguru.UI.TextButton;
import co.uk.epicguru.UI.loading.LoadingSymbol;
import co.uk.epicguru.input.Input;
import co.uk.epicguru.languages.Lan;
import co.uk.epicguru.main.FOE;
import co.uk.epicguru.main.Main;
import ro.fortsoft.pf4j.Extension;

@Extension
public final class MainMenu extends GameScreen {

	public TextureRegion background;
	public TextureRegion title;
	public Observer obs;

	private NinePatch button;
	private float p = 0;
	private float direction = 1;
	private float timer = 0;

	public Image titleImage;
	public TextButton playButton;
	public TextButton pluginsButton;
	public TextButton restartButton;
	public TextButton quitButton;

	private boolean toGame = false;
	private boolean toPlugins = false;

	public void show(){

		this.toGame = false;

		// Loading symbol
		LoadingSymbol.load();

		// Reset UI
		obs = new Observer();

		// Load assets, may be again...
		this.background = Main.INSTANCE.getAsset("Textures/UI/TitleBackground.png", TextureRegion.class);
		this.title = Main.INSTANCE.getAsset("Textures/UI/Title.png", TextureRegion.class);
		this.button = Main.INSTANCE.getAsset("Textures/UI/Button.9.png", NinePatch.class);

		// UI elements
		this.titleImage = new Image(this.title);

		// Play button code
		this.playButton = new TextButton(this.button);
		this.playButton.addLeftClickListener(() -> {
			print("Play button pressed...");

			// Move to game screen from here - AON it does!			
			this.direction = -1;			
			this.toGame = true;
			this.toPlugins = false;
		});

		// Plugins button code
		this.pluginsButton = new TextButton(this.button);
		this.pluginsButton.addLeftClickListener(() -> {
			print("Plugin button pressed...");

			// Move to game screen from here - AON it does!			
			this.direction = -1;			
			this.toGame = false;
			this.toPlugins = true;
		});

		// Restart button code
		this.restartButton = new TextButton(this.button);
		this.restartButton.addLeftClickListener(() -> {
			print("Restart button pressed...");

			// Quit game
			FOE.restart();
		});

		// Quit game button code
		this.quitButton = new TextButton(this.button);
		this.quitButton.addLeftClickListener(() -> {
			print("Quit button pressed...");

			// Quit game
			FOE.exit();
		});

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
		this.button = null;
		this.playButton = null;
		this.restartButton = null;
		this.quitButton = null;
		this.pluginsButton = null;

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
		// IMPORTANT - Disabled until further notice due to graphical glitches!
		//float scale = Gdx.graphics.getWidth() / 1920f;
		obs.setScale(1f); // Something for 4k ?? :D

		// Interpolation effects percentage
		float seconds = 2; // Lasts 2 seconds
		p += (delta * direction) / seconds;
		if(p > 1){
			// End of the line
			p = 1;
		}
		if(p < 0){
			// We may need to change to game screen here
			if(this.toGame){
				FOE.INSTANCE.setScreen(new InGameScreen());
			}
			if(this.toPlugins){
				FOE.INSTANCE.setScreen(new PluginsScreen());				
			}
			p = 0;
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

	public void buttons(float delta, Batch batch){

		Interpolation i = Interpolation.elastic;

		float x = i.apply(-400, 10, this.p);
		float middle = Gdx.graphics.getHeight() / 2f;		

		// Play button
		this.playButton.setColour(Color.WHITE);
		this.playButton.setText(Lan.str("PLAY BUTTON"));
		this.playButton.bounds.set(x, middle, 200, this.playButton.getPatch().getTotalHeight());
		this.playButton.render(obs, delta);

		// Plugins screen button
		this.pluginsButton.setColour(Color.WHITE);
		this.pluginsButton.setText(Lan.str("PLUGINS BUTTON"));
		this.pluginsButton.bounds.set(x, middle - 64, 200, this.pluginsButton.getPatch().getTotalHeight());
		this.pluginsButton.render(obs, delta);

		// Restart button
		this.restartButton.setColour(Color.FIREBRICK);
		this.restartButton.setText(Lan.str("RESTART BUTTON"));
		this.restartButton.bounds.set(x, middle - 64 * 2f, 200, this.restartButton.getPatch().getTotalHeight());
		this.restartButton.render(obs, delta);

		// Quit button
		this.quitButton.setColour(Color.FIREBRICK);
		this.quitButton.setText(Lan.str("QUIT BUTTON"));
		this.quitButton.bounds.set(x, middle - 64 * 3f, 200, this.playButton.getPatch().getTotalHeight());
		this.quitButton.render(obs, delta);

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

		// Buttons
		this.buttons(delta, batch);

		// Hooks
		super.renderUI(delta, batch);
	}

}
