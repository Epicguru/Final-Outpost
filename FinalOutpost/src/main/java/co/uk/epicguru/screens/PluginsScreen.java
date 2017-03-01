package co.uk.epicguru.screens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;

import co.uk.epicguru.API.plugins.FinalOutpostPlugin;
import co.uk.epicguru.API.screens.GameScreen;
import co.uk.epicguru.main.FOE;

public class PluginsScreen extends GameScreen {

	public float[] positions;
	public String[] names;
	public float timer;
	public float totalTime = 3f;
	
	public void update(float delta){
		timer += delta;
		
		float p = timer / totalTime;
		
		for(int i = 0; i < positions.length; i++){
			positions[i] = Interpolation.bounce.apply(getStart(i), getEnd(i), p);
		}
	}
	
	public void show(){
		refresh();
	}
	
	public void refresh(){
		positions = new float[FOE.pluginsLoader.getAllPlugins().length];
		names = new String[positions.length];
		
		int index = 0;
		for(FinalOutpostPlugin plugin : FOE.pluginsLoader.getAllPlugins()){
			names[index++] = plugin.getDisplayName() + " - " + plugin.getDisplayVersion();
		}
		
		timer = 0;
	}
	
	public float getStart(int index){
		return (index * index) * 50f;
	}
	
	public float getEnd(int index){
		return index * 20;
	}
	
	public void render(float delta, Batch batch){
		
	}
	
}
