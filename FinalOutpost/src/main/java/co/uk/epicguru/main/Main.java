package co.uk.epicguru.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import co.uk.epicguru.API.plugins.FinalOutpostPlugin;
import co.uk.epicguru.API.plugins.assets.AssetLoadType;
import co.uk.epicguru.API.plugins.assets.PluginAssetLoader;
import co.uk.epicguru.configs.Config;
import co.uk.epicguru.languages.Lan;
import co.uk.epicguru.languages.utils.LanguagePack;
import co.uk.epicguru.logging.Log;
import co.uk.epicguru.screens.MainMenu;
import ro.fortsoft.pf4j.PluginException;
import ro.fortsoft.pf4j.PluginWrapper;

public class Main extends FinalOutpostPlugin{

	public static Main INSTANCE;
	
	public static Config launch;
	public static Config graphics;
	public static Config lighting;
	
	public static final String version = "ANY";
	public static final String TAG = "Final Outpost Plugin";	
	
	public static final String UP = "Up";
	public static final String DOWN = "Down";
	public static final String LEFT = "Left";
	public static final String RIGHT = "Right";
	public static final String DEBUG = "Debug";
	public static final String VSYNC = "VSync";
	
	public static String lang;
	
	public Main(PluginWrapper wrapper) {
		super(wrapper, "Core", version);
		INSTANCE = this;
	}

	@Override
	public void start() throws PluginException {
		Log.info(TAG, "Started plugin");	
		
		// Create new config
		launch = newConfig("Launch");
		
		launch.add("Title", "Final Outpost by Epicguru");
		launch.add("Language", "English");
		
		// Graphics config
		graphics = newConfig("Graphics");
		
		graphics.add("Windowed Resolution", new Vector2(1200, 600));	
		graphics.add("Fullscreen", false);
		graphics.add("VSync", false);
		
		// Lighting config	
		/*
		 * We need : 
		 * RAYS per light, maybe scalable?
		 * RESOLUTION multiplier, for example 1, 2 or 4
		 * BLUR PASSES around 2 - 10 
		 */
		lighting = newConfig("Lighting");
		
		Log.info(TAG, "Set lighting!");
		
		lighting.add("Rays Per Light", 300);
		lighting.add("Resolution Scale", 1);
		lighting.add("Blur Passes", 3);
	}
	
	public boolean config(Config config){
		
		if(config.is("Launch")){			
			Gdx.graphics.setTitle((String)config.get("Title"));
			lang = (String)config.get("Language");
			
		}
		if(config.is("Graphics")){
			Gdx.graphics.setVSync((boolean)config.get("VSync"));
			
			Vector2 size = (Vector2)config.get("Windowed Resolution");
			Gdx.graphics.setWindowedMode((int)size.x, (int)size.y);
			
			if((boolean)config.get("Fullscreen")){
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
				Log.info(TAG, "Started up in fullscreen mode, @ (" + Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight() + ")");
			}
			
		}
		if(config.is("Lighting")){
			// For now, ignore. This will be read whenever necessary
		}
		
		return true;
	}

	@Override
	public void stop() throws PluginException {
		Log.info(TAG, "Stopping plugin");
	}
	
	@Override
	public boolean loadAssets(PluginAssetLoader loader, AssetLoadType type) {
		
		switch(type){
		case GAME_START:
			
			// Game tiles
			loadAsset("Textures/Map/Dirt.png", TextureRegion.class);
			loadAsset("Textures/Map/Stone.png", TextureRegion.class);
			
			// Player
			loadAsset("Textures/Player/Walk0.png", TextureRegion.class);
			loadAsset("Textures/Player/Walk1.png", TextureRegion.class);
			loadAsset("Textures/Player/Hit0.png", TextureRegion.class);
			loadAsset("Textures/Player/Headshot0.png", TextureRegion.class);
			break;

		case INIT_CORE:
			
			// Main menu content
			loadAsset("Textures/UI/TitleBackground.png", TextureRegion.class);
			loadAsset("Fonts/Default.fnt", BitmapFont.class);
			loadAsset("Fonts/Small.fnt", BitmapFont.class);
			loadAsset("Fonts/Title.fnt", BitmapFont.class);
			loadAsset("Textures/UI/Button.9.png", NinePatch.class);
			
			// Loading icon
			loadAsset("Textures/UI/Loading Cog.png", TextureRegion.class);
			loadAsset("Textures/UI/Loading Ghost.png", TextureRegion.class);
			loadAsset("Textures/UI/Loading Point.png", TextureRegion.class);
			loadAsset("Textures/UI/Loading Square.png", TextureRegion.class);
			loadAsset("Textures/UI/Loading Triangle.png", TextureRegion.class);
			loadAsset("Textures/UI/Title.png", TextureRegion.class);
			
			// Languages
			loadAsset("Lang/English.lan", LanguagePack.class);
			loadAsset("Lang/Spanish.lan", LanguagePack.class);
			
			break;		
		}	
		
		return true;
	}
	
	public boolean loadLanguages(){
		
		Lan.add(getAsset("Lang/English.lan", LanguagePack.class));
		Lan.add(getAsset("Lang/Spanish.lan", LanguagePack.class));
		
		// TODO Save and load?
		Lan.setCurrentLanguage(lang);
		
		return true;
	}
	
	public void init(){
		// INPUTS
		addInput(UP, Keys.W);
		addInput(DOWN, Keys.S);
		addInput(LEFT, Keys.A);
		addInput(RIGHT, Keys.D);
		addInput(DEBUG, Keys.F12);
		addInput(VSYNC, Keys.F1);
	}
	
	public void postInit(){
		FOE.INSTANCE.setScreen(new MainMenu());
	}
}
