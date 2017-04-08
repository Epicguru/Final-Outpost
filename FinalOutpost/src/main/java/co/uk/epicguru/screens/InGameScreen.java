package co.uk.epicguru.screens;

import com.badlogic.gdx.graphics.g2d.Batch;

import co.uk.epicguru.API.screens.GameScreen;
import co.uk.epicguru.entity.engine.Engine;
import co.uk.epicguru.main.Constants;
import co.uk.epicguru.main.FOE;
import co.uk.epicguru.map.GameMap;
import co.uk.epicguru.map.tiles.Tile;
import co.uk.epicguru.physics.JPhysics;
import co.uk.epicguru.screens.hooks.DebugHook;
import co.uk.epicguru.screens.hooks.InputHook;
import ro.fortsoft.pf4j.Extension;

@Extension
public class InGameScreen extends GameScreen {
	
	public void show(){
		// WIP

		// Map
		FOE.map = new GameMap(1000, 1000);
		FOE.map.fill(Tile.getTile("Dirt"));
		
		// Entities
		FOE.engine = new Engine();
		
		// Physics
		JPhysics.reset();
		JPhysics.setPPM(Constants.PPM);
		
		// Add player
		// TODO create player	
		
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
		
		// TODO dispose player
		
		// Clear entities
		FOE.engine.clearEntities();
		FOE.engine = null;
		
		// Physics
		JPhysics.clearWorld();
		
		// Clean up
		System.gc();
		
		super.hide();
	}
	
	public void update(float delta){
		
		FOE.map.update(delta); // Map
		FOE.engine.update(delta); // Entities
		JPhysics.update(delta); // Physics		
		
		super.update(delta);
		
	}
	
	public void render(float delta, Batch batch){
		
		// Camera position
		// TODO set camera pos
		
		FOE.map.render(); // Map
		FOE.engine.render(batch, delta);
		
		super.render(delta, batch);
		
		if(DebugHook.active)
			JPhysics.render(batch, FOE.camera); // Debug
	}
	
	public void renderUI(float delta, Batch batch){	
		
		FOE.engine.renderUI(batch, delta);
		
		super.renderUI(delta, batch);
	}
}
