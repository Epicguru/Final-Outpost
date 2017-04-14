package co.uk.epicguru.screens;

import com.badlogic.gdx.graphics.g2d.Batch;

import co.uk.epicguru.API.screens.GameScreen;
import co.uk.epicguru.entity.components.Position;
import co.uk.epicguru.entity.engine.Engine;
import co.uk.epicguru.main.FOE;
import co.uk.epicguru.map.GameMap;
import co.uk.epicguru.map.tiles.Tile;
import co.uk.epicguru.physics.PhysicsWorldUtils;
import co.uk.epicguru.player.PlayerEntity;
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
		FOE.engine.setWorld(PhysicsWorldUtils.newWorld());
		
		// Physics
		PhysicsWorldUtils.newWorld();
		
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
		
		// Clean up
		System.gc();
		
		super.hide();
	}
	
	public void update(float delta){
		
		FOE.engine.flushBodies(); // Physics bodies bin #1
		FOE.map.update(delta); // Map
		FOE.engine.update(delta); // Entities
		PhysicsWorldUtils.update(delta); // Physics
		FOE.engine.flushBodies(); // Physics bodies bin #2
		
		super.update(delta);		
	}
	
	public void render(float delta, Batch batch){
		
		// Camera position
		Position pos = FOE.player.getComponent(Position.class);
		FOE.camera.position.set(pos.getX(), pos.getY(), 1);
		
		FOE.map.render(); // Map
		FOE.engine.render(batch, delta);
		
		super.render(delta, batch);
		
		if(DebugHook.active){
			PhysicsWorldUtils.render(batch);
		}
	}
	
	public void renderUI(float delta, Batch batch){	
		
		FOE.engine.renderUI(batch, delta);
		
		super.renderUI(delta, batch);
	}
}
