package co.uk.epicguru.screens;

import com.badlogic.gdx.graphics.g2d.Batch;

import co.uk.epicguru.API.screens.GameScreen;
import co.uk.epicguru.entities.EntityManager;
import co.uk.epicguru.main.Constants;
import co.uk.epicguru.main.FOE;
import co.uk.epicguru.map.GameMap;
import co.uk.epicguru.map.tiles.Tile;
import co.uk.epicguru.physics.JPhysics;
import co.uk.epicguru.screens.hooks.DebugHook;
import co.uk.epicguru.screens.hooks.InputHook;
import co.uk.epicguru.screens.hooks.PlayerController;
import ro.fortsoft.pf4j.Extension;

@Extension
public class InGameScreen extends GameScreen {

	private PlayerController player;
	
	public void show(){
		// WIP

		// Map
		FOE.map = new GameMap(1000, 1000);
		FOE.map.fill(Tile.getTile("Dirt"));
		
		// Entities
		EntityManager.clear();
		
		// Physics
		JPhysics.reset();
		JPhysics.setPPM(Constants.PPM);
		
		// Add player
		player = new PlayerController();		
		
		// Hooks
		super.clearHooks();
		super.addHook(new DebugHook());
		super.addHook(new InputHook());
		
		super.show();
	}
	
	public void hide(){
		// TODO save map.
		FOE.map.dispose();
		FOE.map = null;
		player = null;
		EntityManager.clear();
		JPhysics.clearWorld();
		System.gc();
		
		super.hide();
	}
	
	public void update(float delta){
		
		FOE.map.update(delta); // Map
		EntityManager.update(delta); // Entities
		JPhysics.update(delta); // Physics		
		
		super.update(delta);
		
	}
	
	public void render(float delta, Batch batch){
		
		// Camera position
		FOE.camera.position.set(player.body.getX(), player.body.getY(), 0);	
		
		FOE.map.render(); // Map
		EntityManager.render(delta, batch); // Entities
		
		super.render(delta, batch);
		//JPhysics.render(batch, FOE.camera); // Debug
	}
	
	public void renderUI(float delta, Batch batch){	
		
		EntityManager.renderUI(delta, batch);
		
		super.renderUI(delta, batch);
	}
}
