package co.uk.epicguru.API;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;

public class TimersRenderOutput {

	public float[] percentages;
	public float[] radi;
	public Color[] colours;
	
	public static void average(ArrayList<TimersRenderOutput> array, TimersRenderOutput object){
		
		float[] percentages = new float[array.get(0).percentages.length];
		for(TimersRenderOutput out : array){
			for(int x = 0; x < out.percentages.length; x++){
				percentages[x] += out.percentages[x];
			}
		}
		
		for(int x = 0; x < percentages.length; x++){
			percentages[x] /= array.size();
		}
		
		object.percentages = percentages;		
	}
	
}
