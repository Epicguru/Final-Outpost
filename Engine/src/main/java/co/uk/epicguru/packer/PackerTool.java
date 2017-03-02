package co.uk.epicguru.packer;

import java.io.File;
import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisList;
import com.kotcrab.vis.ui.widget.VisTree;
import com.kotcrab.vis.ui.widget.VisWindow;

import co.uk.epicguru.API.U;
import co.uk.epicguru.input.Input;
import co.uk.epicguru.logging.Log;

public class PackerTool extends Game{
	
	private SpriteBatch spr;
	private OrthographicCamera cam;
	private static final String TAG = "Packer Tool";
	private static String loading;
	private BitmapFont font;
	private boolean done;
	private TextureData[] textures;
	private AssetManager assets;
	private Stage stage;
	
	public static void main(String[] args) {
		Game instance = new PackerTool();
		
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Final Outpost - Packer tool");
		new Lwjgl3Application(instance, config);
		
		System.exit(0);
	}
	
	public void refresh(){
		
		clearTextures();
		
		VisUI.load();
		stage = new Stage();
		
		Gdx.input.setInputProcessor(stage);
		
		Log.info(TAG, "Cleared textures");
		
		File root = new File("").getAbsoluteFile().getParentFile().getAbsoluteFile();
		File[] files = U.getAssetFolders(root);
		if(files == null){
			System.exit(-1);
			return;
		}
			
		String removeMe = Gdx.files.getExternalStoragePath();
		String extension = ".png";
		ArrayList<File> images = new ArrayList<File>();
		
		for(File folder : files){
			File[] temp = U.getFilesWithEnding(folder, extension);
			for(File file : temp){
				images.add(file);
			}
		}	
		
		Log.info(TAG, "Found all files...");
		
		Thread thread = new Thread(() -> {
			assets = new AssetManager(new ExternalFileHandleResolver());
			for(File file : images){
				assets.load(file.getAbsolutePath().replace(removeMe, ""), Texture.class);
			}
			
			done = false;
			while(!done){
				// Set loading text
				Gdx.app.postRunnable(() -> {	
					if(done)
						return;
					done = assets.update(10);
					if(!done)
						loading = assets.getProgress() * 100f + "%";
					else{
						loading = "Creating textures...";
					}
				});
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {	}
			}
			
			ArrayList<TextureData> textures = new ArrayList<>();
			for(File f : images){
				Texture t = assets.get(f.getAbsolutePath().replace(removeMe, "").replaceAll("\\\\", "/"), Texture.class);
				textures.add(new TextureData(f, t));
				Log.info(TAG, f.getName());
			}
			
			this.textures = textures.toArray(new TextureData[textures.size()]);
			
			VisTree tree = new VisTree();
			Node[] nodes  = new Node[files.length];
			int i = 0;
			for(File f: files){
				nodes[i++] = new Node(new VisLabel(f.getParentFile().getName()));
			}
			for(i = 0; i < nodes.length; i++){				
				tree.add(nodes[i]);
			}
			VisWindow window = new VisWindow("Textures");
			
			window.add(tree);
			
			stage.addActor(new VisLabel("HEy there!", Color.BLACK));
			stage.addActor(window);
			
			loading = null;
		});
		thread.start();
	}

	@Override
	public void create() {
		font = new BitmapFont();
		spr = new SpriteBatch();
		cam = new OrthographicCamera();
		refresh();
	}
	
	public void clearTextures(){
		if(textures == null)
			return;
		
		for(TextureData data : textures){
			data.dispose();
		}
		textures = null;
		System.gc();
		assets.dispose();
	}
	
	public void update(float delta){
		
		Input.update();
		cam.update();
	}
	
	public void resize(int width, int height){
		cam.setToOrtho(false, width, height);	
		stage.getCamera().viewportWidth = width;
		stage.getCamera().viewportHeight = height;
	}
	
	public void render(){
		update(Gdx.graphics.getDeltaTime());
		
		Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	
		
		spr.setProjectionMatrix(cam.combined);
		spr.begin();
		
		if(loading != null){
			font.draw(spr, loading, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0, 0, false);
		}else{
			if(textures != null){
				stage.act(Gdx.graphics.getDeltaTime());
				spr.end();
				stage.draw();
				spr.begin();
			}
		}
		
		spr.end();
	}
}
