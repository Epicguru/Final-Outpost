package co.uk.epicguru.API.screens;

import com.badlogic.gdx.graphics.g2d.Batch;

import co.uk.epicguru.API.Base;
import co.uk.epicguru.main.FOE;

public abstract class ScreenHook extends Base {

	private GameScreen screen;
	
	public ScreenHook(GameScreen screen){
		this.screen = screen;
	}
	
	public ScreenHook(){
		if(FOE.INSTANCE.getScreen() == null || !(FOE.INSTANCE.getScreen() instanceof GameScreen))
			return;
		this.screen = (GameScreen) FOE.INSTANCE.getScreen();
	}
	
	public GameScreen getScreen(){
		return screen;
	}
	
	public void resize(int width, int height){
		
	}
	
	public void show(){
		
	}
	
	public void hide(){
		
	}
	
	public void update(float delta){
		
	}
	
	public void render(float delta, Batch batch){
		
	}
	
	public void renderUI(float delta, Batch batch){
		
	}
	
	public void pause(){	
		
	}
	
	public void resume(){
		
	}
	
	public void dispose(){
		
	}
}
