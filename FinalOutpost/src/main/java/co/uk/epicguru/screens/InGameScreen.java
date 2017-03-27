package co.uk.epicguru.screens;

import com.badlogic.gdx.graphics.g2d.Batch;

import co.uk.epicguru.API.screens.GameScreen;
import co.uk.epicguru.main.Constants;
import co.uk.epicguru.main.FOE;
import co.uk.epicguru.map.GameMap;
import co.uk.epicguru.map.tiles.Tile;
import co.uk.epicguru.physics.JPhysics;
import co.uk.epicguru.screens.hooks.DebugHook;
import co.uk.epicguru.screens.hooks.PlayerController;
import ro.fortsoft.pf4j.Extension;

@Extension
public class InGameScreen extends GameScreen {

	public void show(){
		// WIP
		// Size?
		FOE.map = new GameMap(1000, 1000);
		FOE.map.fill(Tile.getTile("Dirt"));
		
		JPhysics.reset();
		JPhysics.setPPM(Constants.PPM);
		
		super.clearHooks();
		super.addHook(new DebugHook());
		super.addHook(new PlayerController());
		
		super.show();
	}
	
	public void hide(){
		// TODO save map.
		FOE.map.dispose();
		FOE.map = null;
		System.gc();
		
		super.hide();
	}
	
	public void update(float delta){
		
		super.update(delta);
		JPhysics.update(delta);
		FOE.map.update(delta);
		
	}
	
	public void render(float delta, Batch batch){		
		FOE.map.render();
		super.render(delta, batch);
	}
	
}
