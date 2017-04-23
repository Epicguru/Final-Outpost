package co.uk.epicguru.main;

import static co.uk.epicguru.main.Constants.PPM;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import box2dLight.RayHandler;
import co.uk.epicguru.API.Allocator;
import co.uk.epicguru.API.U;
import co.uk.epicguru.API.plugins.FinalOutpostPlugin;
import co.uk.epicguru.API.plugins.PluginsLoader;
import co.uk.epicguru.API.plugins.assets.AssetLoadType;
import co.uk.epicguru.API.plugins.assets.LanguagePackAssetLoader;
import co.uk.epicguru.API.plugins.assets.NinePatchAssetLoader;
import co.uk.epicguru.API.plugins.assets.PluginAssetLoader;
import co.uk.epicguru.API.plugins.assets.TextureRegionAssetLoader;
import co.uk.epicguru.API.screens.GameScreen;
import co.uk.epicguru.API.screens.core.LoadingScreen;
import co.uk.epicguru.API.screens.core.NoPluginsScreen;
import co.uk.epicguru.IO.JLineParsers;
import co.uk.epicguru.configs.ConfigLoader;
import co.uk.epicguru.entity.Entity;
import co.uk.epicguru.entity.engine.Engine;
import co.uk.epicguru.input.Input;
import co.uk.epicguru.languages.utils.LanguagePack;
import co.uk.epicguru.logging.Log;
import co.uk.epicguru.map.GameMap;
import co.uk.epicguru.map.tiles.Tile;
import co.uk.epicguru.physics.PhysicsWorldUtils;

public class FOE extends Game{

	/**
	 * The instance of the whole game main class.
	 */
	public static FOE INSTANCE;
	public static final String TAG = "Final Outpost Engine";

	public static SpriteBatch batch;
	public static OrthographicCamera camera;
	public static OrthographicCamera UIcamera;
	public static Color BG_Colour = new Color(0.2f, 0.3f, 0.7f, 1f); // BRITAIN TILL THE END!!!!

	public static final String gameDirectory = "Game Data/";
	public static final String inputDirectory = "Input/";
	public static final String gamePluginsExtracted = "Extracted/";
	public static final String pluginsDirectory = "Plugins/";
	public static final String configsDirectory = "Configs/";
	public static final String configsExtension = ".FOConfig";
	public static final String logsDirectory = "Logs/";
	public static final String logsExtension = ".FO_Log";
	public static final String screen_Game = "co.uk.epicguru.screens.InGameScreen";
	public static final String screen_Menu = "co.uk.epicguru.screens.MainMenu";

	public static String loadingText = "PLACEHOLDER";
	public static String loadingSubText = "Something Goes Here!";

	public static boolean loaded = false;
	public static boolean donePluginCheck = false;
	public static boolean postDone = false;
	public static boolean gameJustStarted = true;

	public static PluginsLoader pluginsLoader;
	public static PluginAssetLoader pluginsAssetsLoader;

	public static GameMap map;
	public static Engine engine;
	public static Entity player;

	public static void main(String... args){

		// Debug
		for(String s : args){
			System.out.println(" --" + s);
		}

		// Create game instance
		INSTANCE = new FOE();

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Final Outpost - Loading...");

		new Lwjgl3Application(INSTANCE, config);

		// Done
		System.gc();
		System.exit(0);
	}

	public void create() {

		// DEBUG
		try{
			Log.info(TAG, "This is you game engine speaking, today we will be running on a nice " + Lwjgl3ApplicationConfiguration.getPrimaryMonitor().name + " monitor.");
			Log.info(TAG, Gdx.graphics.getGL30() == null ? "We DO NOT support Gl30" : "We are running Gl30!");
			Log.info(TAG, "GPU - " + Gdx.gl20.glGetString(GL30.GL_RENDERER).split("/")[0]);			
			Log.info(TAG, "GPU Vendor - " + Gdx.gl20.glGetString(GL30.GL_VENDOR));	
			Log.info(TAG, "GPU Version - " + Gdx.gl20.glGetString(GL30.GL_VERSION));
			//Log.info(TAG, "GPU Extensions - " + Gdx.gl20.glGetString(GL30.GL_EXTENSIONS)); // Too detailed, long AF
			Log.info(TAG, "Max memory allocated - " + String.format("%.2f", Runtime.getRuntime().maxMemory() / Math.pow(1024, 3)) + " GB");

		}catch(Exception e){ Log.error(TAG, "Error in debug text.", e); }

		// Required...
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		UIcamera = new OrthographicCamera();

		// Start the allocator
		Allocator.start();

		// Timers names
		final String all = "All";
		final String parsers = "Parsers";
		final String plugins = "Plugins";
		final String pluginsExtraction = "Plugins - Extraction";
		final String assetsLoad = "Assets Load";
		final String packing = "Textures Pack";

		loading("Loading Final Outpost Engine", "Hello there!");

		U.startTimer(all);
		new Thread(() -> {

			// Load plugins
			U.startTimer(plugins);
			PluginsLoader.setupDirectory(gameDirectory + pluginsDirectory);
			pluginsLoader = new PluginsLoader();

			loading("Loading plugins", "Finding on disk...");
			pluginsLoader.loadPlugins();

			// Start plugins
			loading("Loading plugins", "Warming up...");
			pluginsLoader.startPlugins();
			Log.info(TAG, "Loaded and started " + pluginsLoader.getStartedPlugins().size() + " plugins in " + U.endTimer(plugins) + " seconds.");

			// Load parsers
			U.startTimer(parsers);
			loading("Loading plugins' parsers", "...");
			JLineParsers.loadParsers();
			Log.info(TAG, "Loaded parsers in " + U.endTimer(parsers) + " seconds.");
			// Check
			if(JLineParsers.parsers.size() == 0){
				loaded = true;
				return;
			}

			// Loading input keys
			loading("Setting up input", "Just a second M8");
			Input.loadInputs();		

			// Extract assets
			U.startTimer(pluginsExtraction);
			loading("Loading plugin assets", "...");
			pluginsLoader.extractAllAssets();	
			Log.info(TAG, "Extracted assets for all plugins in " + U.endTimer(pluginsExtraction));

			postDone = false;

			// Load configs
			loading("Loading configs", "...");
			Gdx.app.postRunnable(() -> {
				ConfigLoader.loadConfigs();	
				postDone = true;
			}); 

			while(!postDone){
				// Wait, but do not eat up all of CPU
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// Ignored
				}
			}

			// Save configs (For any changes or for default values)
			loading("Saving configs", "...");
			pluginsLoader.saveAllConfigs();

			// Pack all textures
			U.startTimer(packing);
			loading("Optimising (Long packing only happens once)", "...");
			pluginsAssetsLoader = new PluginAssetLoader();
			
			// Special types of data to be loaded
			pluginsAssetsLoader.setLoader(TextureRegion.class, ".png", new TextureRegionAssetLoader(new ExternalFileHandleResolver()));
			pluginsAssetsLoader.setLoader(NinePatch.class, "9.png", new NinePatchAssetLoader(new ExternalFileHandleResolver()));
			pluginsAssetsLoader.setLoader(LanguagePack.class, ".lan", new LanguagePackAssetLoader(new ExternalFileHandleResolver()));
			
			pluginsAssetsLoader.packAllTextures(pluginsLoader);
			Log.info(TAG, "Packed all textures in " + U.endTimer(packing));

			// Load initial assets before init and post init.
			U.startTimer(assetsLoad);
			loading("Loading core content", "");
			pluginsAssetsLoader.loadAllAssets(pluginsLoader, AssetLoadType.INIT_CORE);

			postDone = false;
			while(!postDone){
				float multi = 1f;
				int millis = (int)((1f / 60f) * 1000f * multi);
				Gdx.app.postRunnable(() -> {
					if(pluginsAssetsLoader.update(millis)){
						postDone = true;
					}
					FOE.loadingSubText = (int)(pluginsAssetsLoader.getProgress() * 100f) + "%";
				});				
				try {
					Thread.sleep(millis);
				} catch (InterruptedException e) { }				
			}

			Log.info(TAG, "Loaded " + pluginsAssetsLoader.getLoadedAssets() + " assets in " + U.endTimer(assetsLoad) + " seconds.");

			// Init
			loading("Initialising plugins", "...");
			pluginsLoader.initAllPlugins();
			loading("Post-Initialising plugins", "...");
			pluginsLoader.postInitAllPlugins();

			// Save inputs (also done at shutdown)
			loading("Saving inputs", "Don't blink");
			Input.saveInputs();

			// End timer
			Log.info(TAG, "Finished thread creation in " + U.endTimer(all) + " seconds.");
			loaded = true;
		}).start();

		super.setScreen(new LoadingScreen());		
	}

	public void loading(String title, String sub){
		FOE.loadingText = title;
		FOE.loadingSubText = sub;
	}

	public void setScreen(Screen screen){

		// Custom implementation (WIP)
		Gdx.app.postRunnable(() -> {
			if(screen.getClass().getName().equals(screen_Game)){

				// Entering game screen (world not loaded yet!)
				// Need to load tiles and assets here

				// Clear all assets, if you do not have them saved well then goodbye
				pluginsAssetsLoader.clear();

				// Cleanup
				System.gc();				

				// Load all game assets
				Log.info(TAG, "Switched to game screen, loading assets...");
				pluginsAssetsLoader.loadAllAssets(pluginsLoader, AssetLoadType.GAME_START);

				// And block until they are loaded (TODO)
				pluginsAssetsLoader.finishLoading();
				Log.info(TAG, "Finished quick-load of assets.");

				// Get all tiles
				Tile.registerTiles();

				// Load all tile related assets (load into Tile not into memory as above)
				Tile.gameStart();

				// Cleanup
				System.gc();
			}
			if(screen.getClass().getName().equals(screen_Menu)){
				
				// Only run after first time
				if(!gameJustStarted){
					Log.info(TAG, "Switched to menu screen, loading assets...");
					
					// Clear all game assets
					pluginsAssetsLoader.clear();
					
					// Dispose them
					System.gc();
					
					// Load game starting assets, for menus
					pluginsAssetsLoader.loadAllAssets(pluginsLoader, AssetLoadType.INIT_CORE);
					
					// Finish loading now, TODO
					pluginsAssetsLoader.finishLoading();
					Log.info(TAG, "Finished quick-load of assets.");
				}
				gameJustStarted = false;
			}

			if (this.screen != null) this.screen.hide();
			this.screen = screen;
			if (this.screen != null) {
				Log.info(TAG, "Now showing screen : " + this.screen.getClass().getName());
				this.screen.show();
				this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			}
		});		
	}
	
	public void createEngine(){
		// This needs to be done here due to GL threading stuff...
		if(FOE.engine != null){
			FOE.engine.dispose();
			FOE.engine = null;
		}
		FOE.engine = new Engine();
		FOE.engine.setWorld(PhysicsWorldUtils.newWorld());
		FOE.engine.setRayHandler(new RayHandler(FOE.engine.getWorld()));
	}

	public void update(float delta){

		Input.update();
		camera.update();

		if(!donePluginCheck && loaded){
			FinalOutpostPlugin[] plugins = pluginsLoader.getAllPlugins();

			if(plugins.length == 0){
				NoPluginsScreen s = new NoPluginsScreen();
				s.noPlugins = true;
				this.setScreen(s);
			}else{
				boolean found = false;
				for(FinalOutpostPlugin plugin : plugins){
					if(plugin.getWrapper().getPluginId().equals("FinalOutpost")){
						found = true;
						break;
					}
				}
				if(!found){
					NoPluginsScreen s = new NoPluginsScreen();
					s.noPlugins = false;
					this.setScreen(s);
				}
			}

			donePluginCheck = true;
		}

		Gdx.graphics.setTitle("FPS : " + Gdx.graphics.getFramesPerSecond());

		if(getScreen() != null && getScreen() instanceof GameScreen) ((GameScreen)getScreen()).update(delta);
	}

	public void resize(int width, int height){

		camera.setToOrtho(false, (float)width / PPM, (float)height / PPM);
		UIcamera.setToOrtho(false, width, height); // No scaling

		super.resize(width, height);
	}

	public void render(){

		// Update
		update(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(BG_Colour.r, BG_Colour.g, BG_Colour.b, BG_Colour.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();		
		// Render current screen, normal mode
		super.render();		
		batch.end();		
		batch.setProjectionMatrix(UIcamera.combined);
		batch.begin();	
		// Render current screen, UI mode
		renderUI();	
		batch.end();
	}

	public void renderUI(){
		if(getScreen() == null || !(getScreen() instanceof GameScreen))
			return;

		GameScreen screen = (GameScreen) getScreen();
		screen.renderUI(Gdx.graphics.getDeltaTime(), batch);
	}

	public void dispose(){
		Allocator.stop();
		batch.dispose();		
		pluginsLoader.saveAllConfigs();
		pluginsLoader.stopPlugins();	
		pluginsLoader.dispose();
		pluginsLoader = null;
		Input.saveInputs();
		if(map != null) map.dispose();

		System.gc();

		// WIP
		PluginsLoader.cleanDirectory();

		super.dispose();

		Log.saveLog();
	}
}
