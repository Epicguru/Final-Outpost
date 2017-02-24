package co.uk.epicguru.main;

import com.badlogic.gdx.files.FileHandle;

import co.uk.epicguru.API.plugins.FinalOutpostPlugin;
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
	}

	@Override
	public void stop() throws PluginException {
		Log.info(TAG, "Stopping plugin");
	}
	
	public void contentLoaded(){
		FileHandle d = getAsset("Example.png");
		Log.info(TAG, d.file().getAbsolutePath());
	}
}
