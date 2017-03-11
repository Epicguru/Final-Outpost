package co.uk.epicguru.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;

import co.uk.epicguru.API.screens.GameScreen;
import co.uk.epicguru.input.Input;
import co.uk.epicguru.main.FOE;
import co.uk.epicguru.main.Main;

public class PluginsScreen extends GameScreen {

	public BitmapFont defaultFont;
	
	public float[] positions;
	public String[] names;
	public float timer;
	public float totalTime = 3f;
	
	public void update(float delta){
		timer += delta;
		if(timer > totalTime)
			timer = totalTime;
		
		float p = timer / totalTime;
		
		for(int i = 0; i < positions.length; i++){
			positions[i] = Interpolation.pow4.apply(getStart(i), getEnd(i), p);
		}
		
		if(Input.isKeyJustDown(Keys.SPACE)){
			show();
		}
	}
	
	public void show(){
		
		if(defaultFont == null)
			defaultFont = Main.INSTANCE.getAsset("Fonts/Default.fnt", BitmapFont.class);
		
		refresh();
	}
	
	public void refresh(){
		positions = new float[FOE.pluginsLoader.getAllPlugins().length];
		names = new String[positions.length];
		
		// FinalOutpostPlugin plugin : FOE.pluginsLoader.getAllPlugins()
		// plugin.getDisplayName() + " - " + plugin.getDisplayVersion();
		for(int i = 0; i < names.length; i++){
			names[i] = FOE.pluginsLoader.getAllPlugins()[i].getDisplayName() + " - " + FOE.pluginsLoader.getAllPlugins()[i].getDisplayVersion();
		}
		
		timer = 0;
		totalTime = 1;
	}
	
	public float getStart(int index){
		return ((index + 1) * 50 * (index + 1));
	}
	
	public float getEnd(int index){
		return 30 + index * 40;
	}
	
	public void renderUI(float delta, Batch batch){
		for(int i = 0; i < names.length; i++){			
			defaultFont.draw(batch, names[i], 10, positions[i], 0, -1, false);			
		}
	}
	
}
