package co.uk.epicguru.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import co.uk.epicguru.API.plugins.FinalOutpostPlugin;
import co.uk.epicguru.configs.Config;
import co.uk.epicguru.logging.Log;
import ro.fortsoft.pf4j.PluginException;
import ro.fortsoft.pf4j.PluginWrapper;

public class Main extends FinalOutpostPlugin{

	public static final String version = "0.0.0";
	public static final String TAG = "Final Outpost Plugin";	
	
	public Main(PluginWrapper wrapper) {
		super(wrapper, "Final Outpost Core", version);
	}

	@Override
	public void start() throws PluginException {
		Log.info(TAG, "Started plugin");	
		
		// Create new config
		Config launch = newConfig("Launch");
		
		launch.add("Title", "Final Outpost by Epicguru");
		
		// Graphics config
		Config graphics = newConfig("Graphics");
		
		graphics.add("Windowed Resolution", new Vector2(900, 500));	
		graphics.add("Fullscreen", false);
		graphics.add("VSync", false);
	}
	
	public boolean config(Config config){
		
		if(config.is("Launch")){
			
			Gdx.graphics.setTitle((String)config.read("Title"));
			
		}
		if(config.is("Graphics")){
			Gdx.graphics.setVSync((boolean)config.read("VSync"));
			
			Vector2 size = (Vector2)config.read("Windowed Resolution");
			Gdx.graphics.setWindowedMode((int)size.x, (int)size.y);
			
			if((boolean)config.read("Fullscreen")){
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
				Log.info(TAG, "Started up in fullscreen mode, @ (" + Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight() + ")");
			}
		}
		
		return true;
	}

	@Override
	public void stop() throws PluginException {
		Log.info(TAG, "Stopping plugin");
	}
	
	public void contentLoaded(){
		// TODO
		// FileHandle d = getAsset("Example.png");
	}
}
