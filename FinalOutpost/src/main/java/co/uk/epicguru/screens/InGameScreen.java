package co.uk.epicguru.screens;

import com.badlogic.gdx.graphics.g2d.Batch;

import co.uk.epicguru.API.screens.GameScreen;
import co.uk.epicguru.main.FOE;
import co.uk.epicguru.map.GameMap;

public class InGameScreen extends GameScreen {

	public void show(){
		// WIP
		// Size?
		FOE.map = new GameMap(1000, 1000);
		
		super.show();
	}
	
	public void hide(){
		// TODO save map.
		FOE.map.dispose();
		FOE.map = null;
		System.gc();
		
		super.hide();
	}
	
	public void render(float delta, Batch batch){
		FOE.map.render();
		
		super.render(delta, batch);
	}
	
}
