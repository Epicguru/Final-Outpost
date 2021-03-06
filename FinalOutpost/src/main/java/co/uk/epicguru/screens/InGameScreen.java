package co.uk.epicguru.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;

import co.uk.epicguru.API.screens.GameScreen;
import co.uk.epicguru.entity.components.Position;
import co.uk.epicguru.input.Input;
import co.uk.epicguru.logging.Log;
import co.uk.epicguru.main.FOE;
import co.uk.epicguru.map.GameMap;
import co.uk.epicguru.map.tiles.Tile;
import co.uk.epicguru.map.tiles.TileFactory;
import co.uk.epicguru.physics.PhysicsWorldUtils;
import co.uk.epicguru.player.PlayerEntity;
import co.uk.epicguru.screens.hooks.DebugHook;
import co.uk.epicguru.screens.hooks.InputHook;
import ro.fortsoft.pf4j.Extension;

@Extension
public class InGameScreen extends GameScreen {
	
	public void show(){
		// WIP
		
		// Get config values
//		int rays = (int)Main.lighting.read("Rays Per Light");
//		int scale = (int)Main.lighting.read("Resolution Scale");
//		int passes = (int)Main.lighting.read("Blur Passes");
		
		//IMPORTANT TODO FIXME!
		
		FOE.INSTANCE.createEngine();
		
		FOE.engine.setRaysPerLight(500);
		FOE.engine.setLightResolutionScale(1);
		FOE.engine.setLightBlurPasses(1);
		
		// Physics
		PhysicsWorldUtils.newWorld();
		
		// Map
		FOE.map = new GameMap(1000, 1000);
		FOE.map.fill(Tile.getTile("Dirt"));
		
		TileFactory tile = Tile.getTile("Stone");
		Log.info("", tile.getName());
		for(int x = 0; x < FOE.map.getWidth(); x++){
			for(int y = 0; y < FOE.map.getHeight(); y++){
				if(MathUtils.randomBoolean(0.01f)){
					FOE.map.setTile(tile, x, y);
				}
			}
		}		
		
		// Add player
		FOE.player = new PlayerEntity();
		FOE.engine.add(FOE.player);
		
		// Hooks
		super.clearHooks();
		super.addHook(new DebugHook());
		super.addHook(new InputHook());
		
		super.show();
	}
	
	public void hide(){
		
		// Save and dispose map
		FOE.map.dispose();
		FOE.map = null;
		
		// Dispose player
		FOE.player = null;
		
		// Physics
		PhysicsWorldUtils.removeWorld();
		
		// Clear entities
		FOE.engine.clearEntities();
		FOE.engine = null;
		
		// Physics utils
		PhysicsWorldUtils.dispose();
		
		// Clean up
		System.gc();
		
		super.hide();
	}
	
	public void resize(int width, int height){
		
		FOE.engine.resize(width, height);
		
		// Hooks
		super.resize(width, height);
	}
	
	public void update(float delta){
		
		FOE.engine.flushBodies(); // Physics bodies bin #1
		FOE.map.update(delta); // Map
		FOE.engine.update(delta); // Entities
		PhysicsWorldUtils.update(delta); // Physics
		FOE.engine.flushBodies(); // Physics bodies bin #2
		
		if(Input.isKeyJustDown(Keys.ESCAPE)){
			FOE.INSTANCE.setScreen(new MainMenu());
		}
		
		super.update(delta);		
	}
	
	public void render(float delta, Batch batch){
		
		// Camera position
		Position pos = FOE.player.getComponent(Position.class);
		FOE.camera.position.set(pos.getX(), pos.getY(), 1);
		FOE.camera.update();
		
		FOE.map.render(); // Map
		FOE.engine.render(batch, delta);
		
		super.render(delta, batch);
		
		// Render light now...
		FOE.engine.renderLights(batch, delta);
		
		if(DebugHook.active){
			PhysicsWorldUtils.render(batch);
		}
	}
	
	public void renderUI(float delta, Batch batch){	
		
		FOE.engine.renderUI(batch, delta);
		
		super.renderUI(delta, batch);
	}
}
