package co.uk.epicguru.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import co.uk.epicguru.API.U;
import co.uk.epicguru.API.plugins.PluginsLoader;
import co.uk.epicguru.API.screens.GameScreen;
import co.uk.epicguru.API.screens.core.LoadingScreen;
import co.uk.epicguru.API.screens.core.MainMenu;
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
	public static OrthographicCamera camera;
	public static Color BG_Colour = new Color(0.2f, 0.3f, 0.7f, 1f); // BRITAIN TILL THE END!!!!
	
	public static final String gameDirectory = "Game Data/";
	public static final String gamePluginsExtracted = "Extracted/";
	public static final String pluginsDirectory = "Plugins/";
	public static final String configsDirectory = "Configs/";
	public static final String configsExtension = ".FOConfig";
	public static final String logsDirectory = "Logs/";
	public static final String logsExtension = ".FO_Log";
	
	public static String loadingText = "PLACEHOLDER";
	public static String loadingSubText = "Something Goes Here!";
	public static boolean loaded = false;
	
	public static PluginsLoader pluginsLoader;
	
	public static void main(String... args){
		
		// Debug
		for(String s : args){
			System.out.println(" --" + s);
		}
		
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
		camera = new OrthographicCamera();
		
		// Timers
		final String all = "All";
		final String parsers = "Parsers";
		final String plugins = "Plugins";
		final String pluginsExtraction = "Plugins - Extraction";
		
		loading("Loading Final Outpost Engine", "Hello there!");
		
		U.startTimer(all);
		new Thread(() -> {
			
			// Load plugins
			U.startTimer(plugins);
			PluginsLoader.setupDirectory(gameDirectory + pluginsDirectory);
			pluginsLoader = new PluginsLoader();
			
			loading("Loading plugins", "Finding on disk...");
			pluginsLoader.loadPlugins();
			
			loading("Loading plugins", "Finding on disk...");
			pluginsLoader.startPlugins();
			
			Log.info(TAG, "Loaded and started " + pluginsLoader.getStartedPlugins().size() + " plugins in " + U.endTimer(plugins) + " seconds.");
					
			// Load assets
			// TODO Better place (or implementation) for this?
			U.startTimer(pluginsExtraction);
			loading("Loading plugin assets", "...");
			pluginsLoader.extractAllAssets();	
			Log.info(TAG, "Extracted assets for all plugins in " + U.endTimer(pluginsExtraction));
			
			
			// Load parsers
			U.startTimer(parsers);
			loading("Loading plugins' parsers", "...");
			JLineParsers.loadParsers();
			Log.info(TAG, "Loaded parsers in " + U.endTimer(parsers) + " seconds.");
			
			
			// Load configs
			loading("Loading configs", "...");
			ConfigLoader.loadConfigs();		
			
			// Save configs (For any changes or for default values)
			loading("Saving configs", "...");
			pluginsLoader.saveAllConfigs();
			
			// Init
			loading("Initialising plugins", "...");
			pluginsLoader.initAllPlugins();
			loading("Post-Initialising plugins", "...");
			pluginsLoader.postInitAllPlugins();
			
			// End timer
			Log.info(TAG, "Finished thread creation in " + U.endTimer(all) + " seconds.");
			loaded = true;
			
			// Set main menu
			setScreen(new MainMenu());
		}).start();
		
		super.setScreen(new LoadingScreen());		
	}
	
	public void loading(String title, String sub){
		FOE.loadingText = title;
		FOE.loadingSubText = sub;
	}
	
	public void update(float delta){
		
		camera.update();
		
		if(getScreen() != null && getScreen() instanceof GameScreen) ((GameScreen)getScreen()).update(delta);
	}
	
	public void resize(int width, int height){
		
		camera.setToOrtho(false, width, height); // No scaling (Box2D ?)
		
		super.resize(width, height);
	}
	
	public void render(){
		
		// Update
		update(Gdx.graphics.getDeltaTime());
		
		Gdx.gl.glClearColor(BG_Colour.r, BG_Colour.g, BG_Colour.b, BG_Colour.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		// Render current screen
		super.render();
		
		batch.end();
	}

	public void dispose(){
		batch.dispose();		
		pluginsLoader.saveAllConfigs();
		pluginsLoader.stopPlugins();	
		super.dispose();
		
		Log.saveLog();
	}
	
}
