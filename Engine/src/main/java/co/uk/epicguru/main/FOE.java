package co.uk.epicguru.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import co.uk.epicguru.API.U;
import co.uk.epicguru.IO.JLineParsers;
import co.uk.epicguru.configs.ConfigLoader;
import co.uk.epicguru.logging.Log;

public class FOE extends Game{

	/**
	 * The instance of the whole game main class.
	 */
	public static FOE INSTANCE;
	public static final String TAG = "Final Outpost Engine";
	public static SpriteBatch batch;
	public static Color BG_Colour = new Color(0.2f, 0.3f, 0.7f, 1f); // BRITAIN TILL THE END!!!!
	public static final String gameDirectory = "Game Data/";
	public static final String configsDirectory = "Configs/";
	public static final String configsExtension = ".FOConfig";
	public static final String logsDirectory = "Logs/";
	public static final String logsExtension = ".FO_Log";
	
	public static void main(String... args){
		
		// Debug
		for(String s : args){
			System.out.println(" --" + s);
		}
		
		// Load configs
		// TODO
		
		// Create game instance
		INSTANCE = new FOE();
		
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Final Outpost : Loading");
		new Lwjgl3Application(INSTANCE, config);
		// Done
		System.exit(0);
	}
	
	public void create() {
		
		// Required...
		batch = new SpriteBatch();
		
		// Timers
		final String all = "All";
		final String parsers = "Parsers";
		
		U.startTimer(all);
		new Thread(() -> {
			
			// Load parsers
			U.startTimer(parsers);
			JLineParsers.loadParsers();
			Log.info(TAG, "Loaded parsers in " + U.endTimer(parsers) + " seconds.");
			
			// Load configs
			ConfigLoader.loadConfigs();		
			
			// End timer
			Log.info(TAG, "Finished thread creation in " + U.endTimer(all) + " seconds.");
		}).start();
		
	}
	
	public void render(){
		Gdx.gl.glClearColor(BG_Colour.r, BG_Colour.g, BG_Colour.b, BG_Colour.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	public void dispose(){
		batch.dispose();
		Log.saveLog();
	}
	
}
